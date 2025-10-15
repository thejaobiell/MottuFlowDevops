#!/bin/bash
set -euo pipefail

RG="sprint-mottuflow"
APP_NAME="mottuflow-app"
PLAN_NAME="mottuflow-plan"
LOCATION="brazilsouth"

az group show --name "$RG" >/dev/null 2>&1 || az group create --name "$RG" --location "$LOCATION"

az appservice plan show --name "$PLAN_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
az appservice plan create \
  --name "$PLAN_NAME" \
  --resource-group "$RG" \
  --sku B1 \
  --is-linux

az webapp show --name "$APP_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
az webapp create \
  --resource-group "$RG" \
  --plan "$PLAN_NAME" \
  --name "$APP_NAME" \
  --runtime "JAVA|21-java21" \
  --deployment-local-git

echo "Web App pronto: $APP_NAME"
