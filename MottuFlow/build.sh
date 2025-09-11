#!/bin/bash
set -euo pipefail

RM=554874
RG="rg-cp4-rm${RM}"
ACR_NAME="acrcp4rm${RM}"
IMAGE_NAME="appcp4"
TAG="v1"
LOCATION="brazilsouth"

echo "* Fazendo login na Azure..."
az account show > /dev/null 2>&1 || az login

az group show --name "$RG" > /dev/null 2>&1 || \
    az group create --name "$RG" --location "$LOCATION"

az acr show --name "$ACR_NAME" --resource-group "$RG" > /dev/null 2>&1 || \
    az acr create --resource-group "$RG" --name "$ACR_NAME" --sku Basic --location "$LOCATION"

az acr update --name "$ACR_NAME" --admin-enabled true

az acr login --name "$ACR_NAME"

LOGIN_SERVER=$(az acr show --name "$ACR_NAME" --query loginServer -o tsv)

echo "* Buildando a imagem Docker..."
docker build -t "$LOGIN_SERVER/$IMAGE_NAME:$TAG" .

echo "* Enviando a imagem para o ACR..."
docker push "$LOGIN_SERVER/$IMAGE_NAME:$TAG"

echo "✅ Imagem enviada: $LOGIN_SERVER/$IMAGE_NAME:$TAG"