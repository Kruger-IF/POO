
## 1. Descrição geral da solução

O sistema foi organizado em camadas e pacotes para separar domínio, aplicação, infraestrutura e execução. A estrutura principal ficou assim:

- `app`: classe `Main`, responsável apenas por orquestrar os cenários de demonstração.
- `domain`: entidades e objetos de valor do negócio, como `Usuario`, `Papel`, `Permissao`, `RegistroAcesso` e `RegistroAlteracao`.
- `application`: contratos e serviços de autorização, auditoria e manipulação do domínio.
- `infrastructure`: repositórios em memória para persistir os dados durante a execução.
- `shared`: exceções de domínio reutilizáveis.

A lógica central é simples: permissões são cadastradas, associadas a papéis e, depois, papéis são associados a usuários. A autorização consulta as permissões efetivas do usuário e aplica uma política composta por regras. Toda decisão de acesso é auditada em memória.

## 2. Justificativa do uso de herança

A herança foi usada apenas onde existe relação real de especialização. A classe abstrata `Usuario` representa os dados e comportamentos comuns de qualquer usuário, enquanto `Administrador`, `UsuarioComum` e `UsuarioSistema` especializam o tipo do usuário.

Essa escolha foi feita porque o tipo do usuário faz parte do próprio modelo de domínio. A alternativa seria usar apenas um campo `tipo`, mas isso deixaria a intenção menos clara e enfraqueceria o polimorfismo esperado no enunciado.

A principal limitação dessa abordagem é que, se houver muito comportamento exclusivo por tipo, a hierarquia pode crescer demais. No estado atual, a herança está controlada e pequena, o que reduz esse risco.

## 3. Justificativa do uso de interfaces

As interfaces criadas representam contratos pequenos e coesos:

- repositórios: `UsuarioRepository`, `PapelRepository`, `PermissaoRepository`, `RegistroAcessoRepository`, `RegistroAlteracaoRepository`;
- serviços: `UsuarioService`, `PapelService`, `PermissaoService`, `AutorizacaoService`, `AuditoriaService`;
- política e regras: `PoliticaAcesso` e `RegraAcesso`.

Elas reduzem acoplamento porque o `Main` e os serviços dependem dos contratos, não das classes concretas. Isso permite trocar facilmente a implementação de memória por outra estratégia, se necessário, sem alterar o restante do código.

Exemplo direto: `AutorizacaoServicePadrao` recebe `PoliticaAcesso`, `UsuarioRepository`, `PermissoesEfetivasCalculator` e `AuditoriaService` por abstração. A `PoliticaAcessoPadrao` por sua vez é composta por uma lista de `RegraAcesso` avaliadas em cadeia: `RegraUsuarioBloqueado`, `RegraAdministrador`, `RegraUsuarioSistema` e `RegraPermissaoEfetiva`.

## 4. Uso de composição

A composição aparece em pontos centrais do desenho. Os serviços são montados com repositórios e auditoria por construtor. A política de acesso é composta por uma lista de regras (`RegraUsuarioBloqueado`, `RegraAdministrador`, `RegraUsuarioSistema`, `RegraPermissaoEfetiva`).

Essa composição foi preferida em vez de herança porque o comportamento de autorização varia por regra, não por um único tipo base. Isso facilita a extensão: uma nova regra pode ser adicionada sem alterar as regras existentes.

Também houve composição entre usuário, papéis e permissões. O usuário não guarda permissões diretamente; elas são derivadas a partir dos papéis, o que evita duplicação e mantém o comportamento consistente quando ocorrem remoções.

## 5. Aplicação dos princípios SOLID

### SRP

Cada classe ficou com uma responsabilidade principal. `PermissaoServicePadrao` cadastra permissões, `PapelServicePadrao` manipula papéis, `UsuarioServicePadrao` altera usuários, `AutorizacaoServicePadrao` decide acesso e `AuditoriaServiceEmMemoria` registra e consulta auditoria.

### OCP

A política de acesso é extensível por novas regras. Para adicionar um novo comportamento, basta criar outra implementação de `RegraAcesso` e incluí-la na composição da política.

### LSP

`Administrador`, `UsuarioComum` e `UsuarioSistema` podem ser usados onde `Usuario` é esperado sem quebra de comportamento. O `Main` e os serviços trabalham sempre com o tipo base.

### ISP

As interfaces são pequenas e específicas. Não há um contrato único e genérico para tudo. Isso evita dependências desnecessárias e mantém a leitura simples.

### DIP

Os serviços dependem de abstrações, não de classes concretas. O ponto de montagem no `Main` instancia as implementações em memória, mas o restante do sistema conhece apenas interfaces.

## 6. Aplicação de Object Calisthenics

As regras mais bem perseguidas foram:

- evitar `else`, usando guard clauses e retorno antecipado nas regras de autorização;
- usar objetos de valor para identificadores como `UsuarioId`, `PapelId` e `PermissaoId`;
- manter classes pequenas e com responsabilidade única;
- evitar getters e setters desnecessários, expondo comportamento em vez de estado bruto;
- usar coleções de primeira classe com `Set` e `List` em memória.

A regra mais difícil de seguir integralmente foi a de “um nível de indentação por método”, porque a orquestração do cenário principal naturalmente exige um pouco mais de estrutura. Ainda assim, os métodos ficaram curtos e legíveis.

## 7. Tratamento de erros e validações

As validações de domínio verificam identificadores vazios, nomes vazios, permissões duplicadas, papéis duplicados e referências inexistentes. Quando algo está inválido, o sistema lança `DominioException` com mensagem objetiva.

O bloqueio de usuário é tratado no domínio e refletido imediatamente na autorização. Remoção de papel e remoção de permissão também passam a valer imediatamente porque a autorização consulta o estado atual dos repositórios e das coleções em memória.

## 8. Discussão crítica

As decisões mais importantes foram separar autorização em política composta, modelar usuário por herança justificada e não colocar regra de negócio no `Main`.

As partes que ficaram mais consistentes do ponto de vista OO foram a composição da política de acesso e o modelo de usuário com tipos especializados.

O que ainda pode evoluir: persistência real em banco, testes automatizados, hierarquia de papéis, permissões temporárias e políticas de contexto mais sofisticadas.

Se o sistema fosse para um banco de dados real, os repositórios em memória seriam substituídos por implementações persistentes mantendo os mesmos contratos, o que preservaria a camada de aplicação.

## 9. Limitações conhecidas

- o sistema opera somente em memória;
- não há testes automatizados nesta versão;
- não existe herança entre papéis;
- não foram implementadas permissões temporárias nem políticas baseadas em contexto;
- a aplicação foi pensada para demonstração local no console.
 
