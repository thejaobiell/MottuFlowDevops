set -e

# Variáveis
RM="554874"   
ACR_NAME="acrcp4rm${RM}"
IMAGE_NAME="appcp4"
TAG="v1"

# Login no ACR
az acr login --name $ACR_NAME

# Build da imagem e envio para o ACR
docker build -t $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG .
docker push $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG
