# Sistema de Controle de Permissoes e Acessos

Este arquivo foi criado para registrar, de forma direta, como executar o trabalho com Maven e sem Maven.

## Requisitos
- Java 17 ou superior
- Maven 3.9 ou superior, apenas se for usar a execucao com Maven

## Execucao direta no VS Code
Se o projeto estiver aberto no VS Code com a extensao Java instalada, tambem e possivel abrir o arquivo `Main.java` e clicar em `Run` acima do metodo `main`.
Essa forma usa a configuracao do editor para executar a classe principal sem precisar digitar comandos.

## Execucao com Maven
No diretorio raiz do projeto:

```powershell
mvn -q clean compile
mvn -q exec:java
```

## Execucao sem Maven
No diretorio raiz do projeto:

```powershell
$files = Get-ChildItem -Recurse -Filter *.java src/main/java | ForEach-Object { $_.FullName }
javac --release 17 -d out $files
java -cp out br.edu.poo.permissoes.app.Main
```

## Observacao
A execucao no console demonstra os cenarios obrigatorios do enunciado, incluindo cadastro, associacao, bloqueio, desbloqueio, verificacao de acesso e auditoria em memoria.
