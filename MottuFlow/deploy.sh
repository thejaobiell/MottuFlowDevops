#!/bin/bash
set -euo pipefail

RM=554874
RG="rg-cp4-rm${RM}"
ACR_NAME="acrcp4rm${RM}"
IMAGE_NAME="appcp4"
TAG="v1"
DB_CONTAINER="aci-db-cp4-rm${RM}"
APP_CONTAINER="aci-app-cp4-rm${RM}"
LOCATION="brazilsouth"
DB_USER="rm554874"
DB_PASS="Mottuflow#"

echo "* Login no Azure..."
az account show > /dev/null 2>&1 || az login

LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)
REG_USER=$(az acr credential show --name "$ACR_NAME" --query username -o tsv)
REG_PASS=$(az acr credential show --name "$ACR_NAME" --query passwords[0].value -o tsv)

az container show --resource-group "$RG" --name "$DB_CONTAINER" > /dev/null 2>&1 || \
az container create \
  --resource-group "$RG" \
  --name "$DB_CONTAINER" \
  --image mysql:8.0 \
  --ports 3306 \
  --os-type Linux \
  --cpu 1 \
  --memory 1.5 \
  --dns-name-label "${DB_CONTAINER}-dns" \
  --ip-address public \
  --environment-variables MYSQL_ROOT_PASSWORD="$DB_PASS" MYSQL_DATABASE=mottuflow MYSQL_USER="$DB_USER" MYSQL_PASSWORD="$DB_PASS" \
  --restart-policy Always \
  --location "$LOCATION"

DB_IP=$(az container show --resource-group "$RG" --name "$DB_CONTAINER" --query ipAddress.ip -o tsv)

az container show --resource-group "$RG" --name "$APP_CONTAINER" > /dev/null 2>&1 || \
az container create \
  --resource-group "$RG" \
  --name "$APP_CONTAINER" \
  --image "$LOGIN_SERVER/$IMAGE_NAME:$TAG" \
  --ports 8080 \
  --os-type Linux \
  --cpu 1 \
  --memory 1.5 \
  --dns-name-label "${APP_CONTAINER}-dns" \
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
echo "Usuario: $DB_USER"
echo "Senha: $DB_PASS"
echo "               "
echo "🚀 App acessível em: http://$APP_FQDN:8080"
echo " --------------- " 