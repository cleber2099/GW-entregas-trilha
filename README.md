# Sistema de Gerenciamento de Entregas

Aplicação web em Java que simula o fluxo de gerenciamento de entregas de uma transportadora: cadastro de clientes, produtos, endereços e o controle do ciclo de vida de uma entrega, desde a criação até a confirmação (ou cancelamento).

## Funcionalidades

- Cadastro de **clientes**, cada um com **um ou mais endereços**
- Cadastro de **produtos** com controle de **estoque** (a entrega baixa o estoque; cancelar ou excluir devolve)
- Criação de **entregas** escolhendo um endereço de origem e um de destino (que não podem ser o mesmo)
- Adição e remoção de produtos em uma entrega pendente
- Cálculo automático de peso, volume e valor total da entrega (com o preço da época em que o produto foi adicionado)
- Controle de status: `PENDENTE` → `EM_TRANSITO` → `ENTREGUE`, com possibilidade de `CANCELADA`
- Regras de negócio que impedem alteração de uma entrega fora do status `PENDENTE`

## Estrutura do projeto

```
src/main/
├── java/
│   ├── controllers/   servlets: ProdutoController, ClienteController, EntregaController
│   ├── service/       regras que envolvem o banco: ProdutoService, ClienteService, EntregaService
│   ├── dao/           acesso ao banco (JDBC)
│   ├── models/        Cliente, Endereco, Produto, entrega/Entrega, entrega/ItemEntrega
│   ├── db/            ConnectionFactory
│   └── util/          Validador
├── resources/db/schema.sql
└── webapp/
    ├── WEB-INF/views/ páginas JSP (produtos/, clientes/, entregas/)
    ├── WEB-INF/web.xml
    └── css/estilo.css
```

Camadas no padrão MVC: os **controllers** recebem as requisições, os **services** aplicam as regras usando os **models** e os **DAOs**, e as **views** (JSP) montam o HTML. A explicação completa está em [docs/SISTEMA_WEB.txt](docs/SISTEMA_WEB.txt), e a explicação de cada arquivo do código em [docs/EXPLICACAO_CODIGO.txt](docs/EXPLICACAO_CODIGO.txt).

## Pré-requisitos

- JDK 8 ou superior
- PostgreSQL com o banco `entregasgw` (usuário e senha padrão: `postgres`)
- Tomcat 9

## Como executar

1. Crie as tabelas (isso apaga dados existentes):
   ```bash
   psql -U postgres -d entregasgw -f src/main/resources/db/schema.sql
   ```
2. Gere o pacote:
   ```bash
   ./gradlew war
   ```
3. Copie `build/libs/entregasgw.war` para a pasta `webapps` do Tomcat.
4. Acesse http://localhost:8080/entregasgw/

## Modelo de domínio

| Classe | Responsabilidade |
|---|---|
| `Cliente` | Dados do cliente e seus endereços (pelo menos um) |
| `Endereco` | Endereço de um cliente |
| `Produto` | Produto que pode ser enviado |
| `Entrega` | Origem, destino e itens; controla o status e as regras de alteração |
| `ItemEntrega` | Item de uma entrega (produto + quantidade); gerenciado exclusivamente por `Entrega` |

## Autor

Cléber
