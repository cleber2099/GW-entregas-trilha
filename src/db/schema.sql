
CREATE TABLE endereco (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    rua     VARCHAR(150) NOT NULL,
    numero  VARCHAR(20)  NOT NULL,
    bairro  VARCHAR(100) NOT NULL,
    cidade  VARCHAR(100) NOT NULL,
    estado  VARCHAR(50)  NOT NULL,
    cep     VARCHAR(10)  NOT NULL
);

CREATE TABLE cliente (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    telefone    VARCHAR(20)  NOT NULL,
    documento   VARCHAR(18)  NOT NULL UNIQUE,
    endereco_id INT          NOT NULL,
    CONSTRAINT fk_cliente_endereco
        FOREIGN KEY (endereco_id) REFERENCES endereco (id)
);

CREATE TABLE produto (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(30)    NOT NULL UNIQUE,
    nome   VARCHAR(150)   NOT NULL,
    peso   DECIMAL(10, 3) NOT NULL CHECK (peso > 0),
    volume DECIMAL(10, 3) NOT NULL CHECK (volume > 0),
    valor  DECIMAL(12, 2) NOT NULL CHECK (valor > 0)
);

