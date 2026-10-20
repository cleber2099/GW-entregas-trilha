DROP TABLE IF EXISTS item_entrega CASCADE;
DROP TABLE IF EXISTS entrega CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

CREATE TABLE cliente (
    id        SERIAL PRIMARY KEY,
    nome      VARCHAR(150) NOT NULL,
    telefone  VARCHAR(20)  NOT NULL,
    documento VARCHAR(18)  NOT NULL UNIQUE
);

CREATE TABLE endereco (
    id         SERIAL PRIMARY KEY,
    cliente_id INT          NOT NULL,
    rua        VARCHAR(150) NOT NULL,
    numero     VARCHAR(20)  NOT NULL,
    bairro     VARCHAR(100) NOT NULL,
    cidade     VARCHAR(100) NOT NULL,
    estado     CHAR(2)      NOT NULL,
    cep        VARCHAR(10)  NOT NULL,
    CONSTRAINT fk_endereco_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente (id) ON DELETE CASCADE
);

CREATE TABLE produto (
    id      SERIAL PRIMARY KEY,
    codigo  VARCHAR(30)    NOT NULL UNIQUE,
    nome    VARCHAR(150)   NOT NULL,
    peso    DECIMAL(10, 3) NOT NULL CHECK (peso > 0),
    volume  DECIMAL(10, 3) NOT NULL CHECK (volume > 0),
    valor   DECIMAL(12, 2) NOT NULL CHECK (valor > 0),
    estoque INT            NOT NULL DEFAULT 0 CHECK (estoque >= 0)
);

CREATE TABLE entrega (
    id                  SERIAL PRIMARY KEY,
    codigo              VARCHAR(30) NOT NULL UNIQUE,
    endereco_origem_id  INT         NOT NULL,
    endereco_destino_id INT         NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    CONSTRAINT ck_entrega_status
        CHECK (status IN ('PENDENTE', 'EM_TRANSITO', 'ENTREGUE', 'CANCELADA')),
    CONSTRAINT ck_entrega_enderecos_diferentes
        CHECK (endereco_origem_id <> endereco_destino_id),
    CONSTRAINT fk_entrega_origem
        FOREIGN KEY (endereco_origem_id) REFERENCES endereco (id),
    CONSTRAINT fk_entrega_destino
        FOREIGN KEY (endereco_destino_id) REFERENCES endereco (id)
);

CREATE TABLE item_entrega (
    id             SERIAL PRIMARY KEY,
    entrega_id     INT            NOT NULL,
    produto_id     INT            NOT NULL,
    quantidade     INT            NOT NULL,
    valor_unitario DECIMAL(12, 2) NOT NULL,
    CONSTRAINT uk_item_entrega_produto UNIQUE (entrega_id, produto_id),
    CONSTRAINT fk_item_entrega_entrega
        FOREIGN KEY (entrega_id) REFERENCES entrega (id) ON DELETE CASCADE,
    CONSTRAINT fk_item_entrega_produto
        FOREIGN KEY (produto_id) REFERENCES produto (id)
);
