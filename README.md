# Sistema de Gerenciamento de Entregas

Aplicação Java que simula o fluxo de gerenciamento de entregas de uma transportadora: cadastro de clientes, produtos, endereços e o controle do ciclo de vida de uma entrega, desde a criação até a confirmação (ou cancelamento).

## Funcionalidades

- Cadastro de **clientes** e seus **endereços**
- Cadastro de **produtos**
- Criação de **entregas** vinculadas a um cliente e um endereço de destino
- Adição e remoção de itens em uma entrega
- Cálculo automático do valor total da entrega
- Controle de status da entrega: `PENDENTE` → `EM_TRANSITO` → `ENTREGUE`, com possibilidade de `CANCELADO`
- Regras de negócio que impedem alteração de uma entrega fora do status `PENDENTE`

## Estrutura do projeto

```
src/
├── controllers/
│   ├── ClienteController.java
│   ├── EntregaController.java
│   └── ProdutoController.java
├── models/
│   ├── Cliente.java
│   ├── Endereco.java
│   ├── Entrega.java
│   ├── ItemEntrega.java
│   └── Produto.java
├── App.java
└── Sistema.java
```

- **models/** — entidades do domínio e regras de negócio. `Entrega` é responsável por gerenciar seus próprios itens (`ItemEntrega`), que não são acessíveis fora do pacote `models`.
- **controllers/** — orquestram as operações disponíveis ao usuário (cadastro, consulta, etc.), sem acessar diretamente detalhes internos das entidades.
- **App.java** — ponto de entrada da aplicação.
- **Sistema.java** — classe central que integra os controllers.

## Pré-requisitos

- JDK 8 ou superior instalado ([ajuste esse número para a versão que você está usando])

## Como executar

### Pela linha de comando

```bash
# a partir da raiz do projeto
javac -d bin -cp "lib/*" src/**/*.java src/**/**/*.java
java -cp "bin:lib/*" App
```

> No Windows, troque `:` por `;` no classpath (`bin;lib/*`).

### Pelo VS Code

1. Abra a pasta do projeto no VS Code com a extensão *Extension Pack for Java* instalada.
2. Abra o arquivo `App.java`.
3. Clique em **Run** (▷) acima do método `main`, ou pressione `F5`.

## Modelo de domínio

| Classe | Responsabilidade |
|---|---|
| `Cliente` | Dados do cliente e seu endereço |
| `Endereco` | Endereço de entrega |
| `Produto` | Produto disponível para compra |
| `Entrega` | Agrega cliente, endereço e itens; controla status e regras de alteração |
| `ItemEntrega` | Item de uma entrega (produto + quantidade); gerenciado exclusivamente por `Entrega` |

## Autor

Cléber
