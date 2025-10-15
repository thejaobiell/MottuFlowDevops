# MottuFlow - Detalhamento dos Componentes

## 📋 Tabela de Componentes da Arquitetura

| Nome do Componente | Tipo | Descrição Funcional | Tecnologia/Ferramenta |
|-------------------|------|---------------------|----------------------|
| **GitHub Repository** | SCM (Source Control Management) | Repositório central onde o código-fonte da aplicação é versionado. Controla todas as alterações e mantém histórico completo do projeto | GitHub |
| **Branch Master** | Branch Principal | Branch de produção que aciona automaticamente o pipeline de CI/CD através de webhooks quando recebe novos commits | Git |
| **Pipeline Variables** | Configuração Segura | Armazena credenciais sensíveis (senhas do banco de dados, tokens de acesso, chaves de API) de forma criptografada para uso nos pipelines | Azure DevOps Variables (Protected) |
| **CI Pipeline - Trigger** | Gatilho Automático | Detecta automaticamente pushes na branch master e inicia o processo de integração contínua | Azure DevOps Pipelines |
| **CI Pipeline - Checkout** | Etapa de CI | Clona o código-fonte do repositório GitHub para o ambiente de build do Azure DevOps | Azure DevOps Checkout Task |
| **CI Pipeline - Build Maven** | Compilação | Compila o código Java, executa testes unitários e gera o arquivo JAR executável da aplicação | Apache Maven 3.9+ / Java 21 |
| **CI Pipeline - Docker Build** | Containerização | Cria a imagem Docker da aplicação usando o Dockerfile, empacotando a aplicação Java e suas dependências | Docker Engine |
| **CI Pipeline - Push to ACR** | Publicação de Imagem | Faz upload da imagem Docker buildada para o Azure Container Registry com tag `latest` | Azure Container Registry (ACR) |
| **CI Pipeline - Publish Artifact** | Armazenamento | Publica artefatos de build (JAR, scripts, configurações) para uso posterior no pipeline de CD | Azure DevOps Artifacts |
| **CD Pipeline - Trigger** | Gatilho Automático | Inicia automaticamente o processo de deploy após conclusão bem-sucedida do pipeline de CI | Azure DevOps Release Pipeline |
| **CD Pipeline - Download** | Download de Artefatos | Baixa os artefatos publicados pelo pipeline de CI para preparar o deploy | Azure DevOps Download Task |
| **CD Pipeline - Deploy ACI** | Deploy em Produção | Provisiona containers no Azure (MySQL e aplicação), configura networking, DNS e expõe a aplicação publicamente | Azure Container Instances (ACI) |
| **Azure Container Registry** | Registro de Imagens | Repositório privado de imagens Docker no Azure. Armazena versões da aplicação containerizada | Azure Container Registry (ACR) |
| **Azure Container Instances** | Ambiente de Execução | Executa os containers da aplicação (Spring Boot) e banco de dados (MySQL) em produção na nuvem Azure | Azure Container Instances (ACI) |
| **MySQL Database** | Banco de Dados Relacional | Armazena todos os dados da aplicação (funcionários, motos, pátios, câmeras, etc.) com persistência garantida | MySQL 8.0 |
| **Spring Boot Application** | Framework Backend | Core da aplicação, gerencia toda a lógica de negócios, roteamento de requisições e integração com banco de dados | Spring Boot 3.4.5 (Java 21) |
| **Spring Security + JWT** | Autenticação e Autorização | Gerencia autenticação de usuários, geração de tokens JWT e controle de acesso aos endpoints da API | Spring Security 6 + JWT |
| **Flyway Migration** | Versionamento de BD | Executa migrations automáticas do banco de dados no deploy, garantindo que o schema esteja sempre atualizado | Flyway 9.x |
| **Thymeleaf Templates** | Engine de Templates | Renderiza as páginas HTML do dashboard administrativo do lado do servidor | Thymeleaf 3.1 |
| **Web Interface** | Interface Administrativa | Interface web para gerenciamento da frota, visualização de dashboards e administração do sistema | HTML5 + CSS3 + JavaScript + Thymeleaf |
| **REST API** | Interface de Integração | API RESTful completa para integração com aplicações mobile e front-ends externos, com autenticação JWT | Spring Web MVC |

---

## 🎭 Personas e Suas Interações

| Persona | Papel | Interação com o Sistema |
|---------|-------|------------------------|
| **👨‍💻 Desenvolvedor** | Desenvolvedor de Software | Realiza commits no GitHub, monitora pipelines, corrige bugs e implementa novas funcionalidades |
| **👤 Usuário Final** | Funcionário/Gerente | Acessa a interface web via HTTPS para gerenciar motos, funcionários, pátios e visualizar monitoramento |
| **👔 Administrador** | DevOps/SysAdmin | Monitora infraestrutura Azure, gerencia pipelines no Azure DevOps, controla custos e recursos cloud |

---

## 🔄 Fluxo Completo CI/CD (Ordem das Etapas)

1. **Desenvolvedor** → git push → GitHub Repository
2. **Azure Devops** → detecta alterações via integração GitHub  → **Pipeline CI Trigger**
3. **Checkout** do código-fonte do repositório GitHub
4. **Build Maven** (compilação + testes)
5. **Docker Build** (criação da imagem containerizada)
6. **Push to ACR** (publicação da imagem no Azure Container Registry)
7. **Publish Artifact** (armazenamento de artefatos de build)
8. **CD Trigger** (início automático do deploy)
9. **Download Artifact** (recuperação de artefatos)
10. **Deploy to ACI** (provisionamento de containers MySQL e aplicação no Azure)
11. **Aplicação disponível** para usuários finais via HTTP (porta 8080)

---

## 🛠️ Scripts de Automação

| Script | Tipo | Descrição Funcional | Tecnologia |
|--------|------|---------------------|-----------|
| **build.sh** | Script de Build | Cria Resource Group, configura ACR, faz build da imagem Docker e push para ACR | Bash + Azure CLI |
| **deploy.sh** | Script de Deploy | Provisiona containers MySQL e aplicação no ACI, configura DNS e networking | Bash + Azure CLI |
| **limpar.sh** | Script de Limpeza | Remove todos os recursos Azure do projeto para evitar custos desnecessários | Bash + Azure CLI |

---

## 📦 Dependências e Bibliotecas

| Componente | Versão | Finalidade |
|-----------|--------|-----------|
| Java | 21 LTS | Linguagem de programação principal |
| Spring Boot | 3.4.5 | Framework de aplicação |
| Spring Data JPA | 3.4.5 | Persistência e ORM para banco de dados |
| Spring Security | 3.4.5 | Framework de segurança e autenticação |
| Spring Web | 3.4.5 | Desenvolvimento de APIs RESTful |
| Spring Validation | 3.4.5 | Validação de dados de entrada |
| Spring Actuator | 3.4.5 | Monitoramento e métricas da aplicação |
| Thymeleaf | 3.4.5 | Engine de templates para renderização HTML |
| Auth0 Java-JWT | 4.5.0 | Geração e validação de tokens JWT |
| Flyway Core | Latest | Versionamento e migração de banco de dados |
| Flyway MySQL | Latest | Driver específico Flyway para MySQL |
| MySQL Connector/J | Latest (runtime) | Driver JDBC para conexão com MySQL |
| Lombok | Latest | Redução de boilerplate code (getters, setters, construtores) |
| SpringDoc OpenAPI UI | 2.8.11 | Documentação automática da API (Swagger UI) |
| SpringDoc OpenAPI Data REST | 1.7.0 | Integração OpenAPI com Spring Data REST |
| Spring DevTools | Latest (runtime) | Hot reload e ferramentas de desenvolvimento |
| Maven | 3.9+ | Gerenciamento de dependências e build |
| Docker | 20.0+ | Containerização da aplicação |
| Azure CLI | 2.0+ | Automação de recursos Azure |

---

## 🌐 Endpoints de Rede

| Endpoint | Porta | Protocolo | Finalidade |
|----------|-------|-----------|-----------|
| Web Interface | 8080 | HTTP | Dashboard administrativo |
| REST API | 8080 | HTTP | Endpoints da API REST |
| MySQL Database | 3306 | TCP | Conexão com banco de dados |

---

## 💾 Estrutura de Dados

| Entidade | Descrição | Principais Campos |
|----------|-----------|------------------|
| Funcionários | Colaboradores da empresa | ID, Nome, CPF, Email, Senha, Telefone, Cargo, Refresh_Token, Expiracao_Refresh_Token |
| Pátios | Localidades de estacionamento | ID, Nome, Endereço, Capacidade |
| Motos | Veículos da frota | ID, Placa, Modelo, Fabricante, Ano, Localização |
| Câmeras | Sistema de monitoramento | ID, Localização, Status_Operacional |
| ArUco Tags | Tags de identificação | ID, Código, Moto Vinculada |
| Localidade | Registro de posicionamento em tempo real | ID, Data/Hora, Ponto de Referência, ID Moto (FK), ID Pátio (FK), ID Câmera (FK) |
| Registro Status | Histórico de estados das motos | ID, Tipo Status (DISPONIVEL, MANUTENÇÃO, RESERVADO, BAIXA_BO), Descrição, Data Status, ID Moto (FK), ID Funcionário (FK) |
| Usuários | Acesso ao sistema | ID, Email, Senha (hash), Role |

---

## 🔐 Segurança

| Componente | Mecanismo | Descrição |
|-----------|-----------|-----------|
| Autenticação | JWT (JSON Web Token) | Tokens com expiração para acesso à API |
| Autorização | Spring Security | Controle de acesso baseado em roles |
| Senhas | BCrypt Hashing | Senhas armazenadas com hash BCrypt |
| Credenciais | Azure Key Vault / Pipeline Variables | Armazenamento seguro de segredos |
