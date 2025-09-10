set -e

RM="554874"
RG="rg-cp4-rm${RM}"
ACR_NAME="acrcp4rm${RM}"
IMAGE_NAME="appcp4"
TAG="v1"
DB_CONTAINER="aci-db-cp4-rm${RM}"
APP_CONTAINER="aci-app-cp4-rm${RM}"
DB_PASSWORD="RM#554874"

az group create --name $RG --location brazilsouth

az container create \
  --resource-group $RG \
  --name $DB_CONTAINER \
  --image mysql:8.0 \
  --ports 3306 \
  --environment-variables MYSQL_ROOT_PASSWORD=$DB_PASSWORD MYSQL_DATABASE=mottuflow MYSQL_USER=user MYSQL_PASSWORD=$DB_PASSWORD \
  --restart-policy Always

DB_IP=$(az container show --resource-group $RG --name $DB_CONTAINER --query ipAddress.ip --output tsv)

az container create \
  --resource-group $RG \
  --name $APP_CONTAINER \
  --image $ACR_NAME.azurecr.io/$IMAGE_NAME:$TAG \
  --ports 8080 \
  --environment-variables DB_HOST=$DB_IP DB_PORT=3306 DB_NAME=mottuflow DB_USER=user DB_PASSWORD=$DB_PASSWORD \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $(az acr credential show --name $ACR_NAME --query username -o tsv) \
  --registry-password $(az acr credential show --name $ACR_NAME --query passwords[0].value -o tsv) \
  --restart-policy Always
