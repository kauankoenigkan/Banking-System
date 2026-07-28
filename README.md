# 🏦 Sistema Bancário Básico

<p align="left">
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Database-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Server-XAMPP-FB7A24?style=for-the-badge&logo=xampp&logoColor=white" alt="XAMPP">
  <img src="https://img.shields.io/badge/Security-SHA--256-000000?style=for-the-badge&logo=lock&logoColor=white" alt="SHA-256">
</p>

---

## 🚀 Tecnologias e Ferramentas

* **Linguagem Principal:** Java (JDK 8 ou superior)
* **Banco de Dados:** MySQL / MariaDB (via XAMPP)
* **Gerenciador de Banco de Dados:** phpMyAdmin
* **Conectividade:** JDBC (`com.mysql.cj.jdbc.Driver`)
* **Criptografia:** Algoritmo SHA-256 (`java.security.MessageDigest`)
* **Terminal / CLI:** PowerShell / Bash / Command Prompt
* **IDE Recomendada:** VS Code / Eclipse / IntelliJ IDEA

---

## 📌 Funcionalidades

* 🔐 **Autenticação Segura:** Login realizado por Nome de Titular e Senha.
* 👤 **Cadastro de Usuários:** Registro de novas contas garantindo que não existam titulares duplicados (`UNIQUE`).
* 🆔 **Auto-Incremento Nativo:** Números de contas gerados automaticamente pelo MySQL (`AUTO_INCREMENT`).
* 💵 **Depósitos:** Saldo atualizado e persistido instantaneamente no banco de dados.
* 🏧 **Saques:** Validação automática de saldo suficiente antes de efetivar a transação.
* 📋 **Extrato em Tempo Real:** Exibição do titular, número da conta e saldo atualizado.

---

## ⚡ Diferenciais Técnicos (Boas Práticas)

* **Arquitetura Modular:** Divisão clara entre o fluxo de controle CLI (`Main.java`), a regra de negócio do domínio (`ContaBancaria.java`) e o utilitário de segurança (`SecurePassword.java`).
* **Segurança de Senhas (Hash Irreversível):** As senhas nunca são salvas em texto puro. Elas são convertidas em um Hash SHA-256 de 64 caracteres antes da inserção no banco.
* **Proteção contra SQL Injection:** Uso estrito de `PreparedStatement` em todas as interações SQL com o MySQL.
* **Captura Nativa de Chaves Geradas:** Resgate do ID gerado no auto-incremento utilizando `Statement.RETURN_GENERATED_KEYS` diretamente no JDBC, dispensando novas consultas SELECT.
* **Versionamento Limpo:** Repositório livre de arquivos compilados (`.class`) e dependências binárias (`.jar`), utilizando `.gitignore` padrão Java.

---

## 📂 Estrutura do Projeto

```text
Banking-System/
├── .gitignore                  # Arquivos ignorados pelo Git (.class, .jar, etc)
├── schema.sql                  # Script SQL para criação da base e tabela
└── scr/
    ├── ContaBancaria.java      # Modelo de dados e validações da conta
    ├── Main.java               # Classe principal e fluxo do menu CLI
    ├── README.md 
    └── SecurePassword.java     # Módulo de criptografia de senhas (SHA-256)
```

## 🗄️ Configuração do Banco de Dados (MySQL / XAMPP)

1. Abra o XAMPP Control Panel e inicie o serviço MySQL (porta padrão 3306).

2. Acesse o phpMyAdmin em seu navegador (http://localhost/phpmyadmin).

3. Vá para a aba SQL e execute o script abaixo:

```text
SQL
CREATE DATABASE IF NOT EXISTS bancoDB;
USE bancoDB;

CREATE TABLE IF NOT EXISTS contas (
    numero_conta INT AUTO_INCREMENT PRIMARY KEY,
    titular VARCHAR(100) UNIQUE NOT NULL,
    saldo DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    senha_hash VARCHAR(64) NOT NULL
);
```

## 📦 Como Baixar as Dependências e Executar

1. Pré-requisito: Download do MySQL Connector/J
   
* Baixe o driver JDBC oficial no site do MySQL: Connector/J Download.
* Escolha Platform Independent (Zip Archive) e extraia o arquivo.
* Copie o arquivo .jar extraído (exemplo: mysql-connector-j-9.0.0.jar) e cole dentro da pasta scr/ do seu projeto.

2. Execução via Terminal
   
```bash
# 1. Clone o repositório
git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)

# 2. Acesse a pasta do código-fonte
cd Banking-System/scr

# 3. Compile todos os arquivos incluindo o driver na classpath
javac -cp ".;mysql-connector-j-9.0.0.jar" *.java

# 4. Execute a aplicação
java -cp ".;mysql-connector-j-9.0.0.jar" Main
```

## 👨‍💻 Autor

Desenvolvido por Kauan.
