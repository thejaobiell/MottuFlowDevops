#!/bin/bash
set -euo pipefail

RM=554874
RG="rg-cp4-rm${RM}"

echo "⚠️ Deletando o Resource Group $RG e todos os recursos associados..."
az group delete --name "$RG" --yes --no-wait

echo "✅ Comando enviado. A exclusão acontece em background."
