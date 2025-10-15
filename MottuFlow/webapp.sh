#!/bin/bash
set -euo pipefail

RM=554874
RG="sprint-mottuflow"
APP_NAME="mottuflow-app"
PLAN_NAME="mottuflow-plan"
LOCATION="brazilsouth"

ACR_NAME="mottuflow"
DB_CONTAINER="mottuflow-db"

DB_USER=${DB_USER:?DB_USER não definido}
DB_PASS=${DB_PASS:?DB_PASS não definido}

command -v az >/dev/null 2>&1 || { echo "Azure CLI não encontrada. Abortando."; exit 1; }

# ----------------- Resource Group -----------------
az group show --name "$RG" >/dev/null 2>&1 || az group create --name "$RG" --location "$LOCATION"

# ----------------- App Service Plan -----------------
az appservice plan show --name "$PLAN_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
az appservice plan create \
  --name "$PLAN_NAME" \
  --resource-group "$RG" \
  --sku B1 \
  --is-linux

# ----------------- Web App -----------------
az webapp show --name "$APP_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
az webapp create \
  --resource-group "$RG" \
  --plan "$PLAN_NAME" \
  --name "$APP_NAME" \
  --runtime "JAVA|21-java21" \
  --deployment-local-git

# ----------------- ACR -----------------
az acr show --name "$ACR_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
az acr create --resource-group "$RG" --name "$ACR_NAME" --sku Basic --location "$LOCATION"

az acr update --name "$ACR_NAME" --admin-enabled true
LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)
REG_USER=$(az acr credential show --name "$ACR_NAME" --query username -o tsv)
REG_PASS=$(az acr credential show --name "$ACR_NAME" --query passwords[0].value -o tsv)

# ----------------- Banco MySQL no ACI -----------------
az container show --resource-group "$RG" --name "$DB_CONTAINER" >/dev/null 2>&1 || \
az container create \
  --resource-group "$RG" \
  --name "$DB_CONTAINER" \
  --image mysql:8.0 \
  --ports 3306 \
  --os-type Linux \
  --cpu 1 \
  --memory 1.5 \
  --dns-name-label "${DB_CONTAINER}-dns-${RM}" \
  --ip-address public \
  --environment-variables MYSQL_ROOT_PASSWORD="$DB_PASS" MYSQL_DATABASE=mottuflow MYSQL_USER="$DB_USER" MYSQL_PASSWORD="$DB_PASS" \
  --restart-policy Always \
  --location "$LOCATION"

DB_IP=$(az container show --resource-group "$RG" --name "$DB_CONTAINER" --query ipAddress.ip -o tsv)

echo " --------------- "
echo "Web App pronto: $APP_NAME"
echo "DB rodando em: $DB_IP"
echo "Usuário: $DB_USER"
echo "Senha: $DB_PASS"
echo "Banco de dados: mottuflow"
echo " --------------- "
