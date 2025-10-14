#!/bin/bash
set -euo pipefail

RM=554874
RG="sprint-mottuflow"
ACR_NAME="mottuflow"
IMAGE_NAME="javamottuflow"
TAG="v1"
DB_CONTAINER="mottuflow-db"
APP_CONTAINER="mottuflow-app"
LOCATION="brazilsouth"

DB_USER=${DB_USER:?DB_USER não definido}
DB_PASS=${DB_PASS:?DB_PASS não definido}

echo "* Login no Azure..."
az account show > /dev/null 2>&1 || az login

echo "* Buscando credenciais do ACR..."
LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)
REG_USER=$(az acr credential show --name "$ACR_NAME" --query username -o tsv)
REG_PASS=$(az acr credential show --name "$ACR_NAME" --query passwords[0].value -o tsv)

echo "* Criando container do MySQL (se não existir)..."
az container show --resource-group "$RG" --name "$DB_CONTAINER" > /dev/null 2>&1 || \
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

echo "* Criando container da aplicação (se não existir)..."
az container show --resource-group "$RG" --name "$APP_CONTAINER" > /dev/null 2>&1 || \
az container create \
  --resource-group "$RG" \
  --name "$APP_CONTAINER" \
  --image "$LOGIN_SERVER/$IMAGE_NAME:$TAG" \
  --ports 8080 \
  --os-type Linux \
  --cpu 1 \
  --memory 1.5 \
  --dns-name-label "${APP_CONTAINER}-dns-${RM}" \
  --ip-address public \
  --environment-variables DB_HOST="$DB_IP" DB_PORT=3306 DB_NAME=mottuflow DB_USER="$DB_USER" DB_PASSWORD="$DB_PASS" SERVER_PORT=8080 \
  --registry-login-server "$LOGIN_SERVER" \
  --registry-username "$REG_USER" \
  --registry-password "$REG_PASS" \
  --restart-policy Always \
  --location "$LOCATION"

APP_FQDN=$(az container show --resource-group "$RG" --name "$APP_CONTAINER" --query ipAddress.fqdn -o tsv)

echo " --------------- "
echo "DB rodando em: $DB_IP"
echo "Usuário: $DB_USER"
echo "Senha: $DB_PASS"
echo "Banco de dados: mottuflow"
echo
echo "🚀 App acessível em: http://$APP_FQDN:8080"
echo " --------------- "
