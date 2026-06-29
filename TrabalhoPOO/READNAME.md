# Sistema de Controle de Permissoes e Acessos

## Requisitos

- Java 17 ou superior
- Maven 3.9 ou superior (opcional — apenas para execucao via Maven)

## Execucao no VS Code

Abra o projeto no VS Code com a extensao **Extension Pack for Java** instalada.  
Navegue ate o arquivo `src/main/java/br/edu/poo/permissoes/app/Main.java` e clique em **Run** acima do metodo `main`.

## Execucao com Maven

No diretorio raiz do projeto (onde esta o `pom.xml`), execute:

```bash
mvn clean compile exec:java
```

Ou em dois passos:

```bash
mvn clean compile
mvn exec:java
```

## Observacao

A execucao no console demonstra todos os cenarios obrigatorios do enunciado:
cadastro, associacao de papeis e permissoes, bloqueio, desbloqueio,
verificacao de acesso por cadeia de regras e auditoria em memoria.
