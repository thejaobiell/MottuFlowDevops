set -e

RM="554874"   
ACR_NAME="acrcp4rm${RM}"
IMAGE_NAME="appcp4"
TAG="v1"

az acr login --name $ACR_NAME

docker build -t $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG .
docker push $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG