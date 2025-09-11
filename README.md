# Checkpoint 4 - DevOps & Cloud

## 👥 Identificação do Grupo

- João Gabriel Boaventura Marques e Silva - RM 554874
- Lucas Leal das Chagas - RM 551124

---

## 🏢 Sobre a Aplicação

MottuFlow é uma aplicação híbrida desenvolvida em Java, projetada para gerenciar de forma completa funcionários, pátios, motos, câmeras, ArUco tags, status das motos e localidades. Ela combina:

API REST: para integração com front-ends externos (como aplicativos mobile em React Native).

Interface web com Thymeleaf: para uso direto pelo navegador, com páginas de gerenciamento e visualização dos dados.

---

## 📂 Estrutura do Repositório

```
.
├── Dockerfile
├── docker-compose.yml ( para o teste local )
├── build.sh
├── deploy.sh
├── limpar.sh
├── README.md
└── src/ (código da aplicação)
````

---

## ⚙️ Scripts

### 1️⃣ build.sh

- Cria o **Resource Group** e o **ACR** (se não existirem)  
- Faz **login no ACR**  
- Builda a imagem Docker da aplicação  
- Faz **push da imagem para o ACR**

```bash
./build.sh
````

### 2️⃣ deploy.sh

* Cria o container do **MySQL** no ACI
* Cria o container da **aplicação Java** no ACI apontando para o banco
* Configura IP público e DNS para acesso externo

```bash
./deploy.sh
```

Após rodar, o script imprime o FQDN público da aplicação:

```
🚀 App acessível em: http://<FQDN>:8080
```

### 3️⃣ limpar.sh

* Deleta o **Resource Group** e todos os recursos associados

```bash
./limpar.sh
```

---

## 🔧 Variáveis

Os scripts já possuem todas as variáveis configuradas, incluindo:

* RM do grupo
* Nomes padronizados do Resource Group, ACR, containers
* Senha do banco
* Localização: `brazilsouth`
* CPU/memória dos containers
* Porta da aplicação: `8080`

---

## 🚀 Testando a Aplicação

1. Execute o build e push da imagem:

```bash
./build.sh
```

2. Suba o banco e a aplicação:

```bash
./deploy.sh
```

3. Copie o FQDN impresso pelo script e acesse no navegador:

```
http://<FQDN>:8080
```

4. Verifique se a aplicação está conectada ao banco de dados e funcionando corretamente.

---

## 🧹 Limpeza dos Recursos

Após validar o funcionamento, execute:

```bash
./limpar.sh
```

> Isso evita custos desnecessários no Azure, removendo o Resource Group e todos os recursos.
