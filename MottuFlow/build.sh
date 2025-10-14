#!/bin/bash
set -euo pipefail

RM=554874
RG="sprint-mottuflow"
ACR_NAME="mottuflow"
IMAGE_NAME="javamottuflow"
TAG="v1"
LOCATION="brazilsouth"

command -v az >/dev/null 2>&1 || { echo "Azure CLI não encontrada. Abortando."; exit 1; }

command -v docker >/dev/null 2>&1 || { echo "Docker não encontrado. Abortando."; exit 1; }

echo "* Criando Resource Group (se não existir)..."
az group show --name "$RG" >/dev/null 2>&1 || \
    az group create --name "$RG" --location "$LOCATION"

echo "* Criando ACR (se não existir)..."
az acr show --name "$ACR_NAME" --resource-group "$RG" >/dev/null 2>&1 || \
    az acr create --resource-group "$RG" --name "$ACR_NAME" --sku Basic --location "$LOCATION"

echo "* Habilitando admin no ACR..."
az acr update --name "$ACR_NAME" --admin-enabled true

echo "* Logando no ACR..."
az acr login --name "$ACR_NAME"

LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)

echo "* Buildando a imagem Docker..."
docker build -t "$LOGIN_SERVER/$IMAGE_NAME:$TAG" .

echo "* Enviando a imagem para o ACR..."
docker push "$LOGIN_SERVER/$IMAGE_NAME:$TAG"

echo "Imagem enviada: $LOGIN_SERVER/$IMAGE_NAME:$TAG"
