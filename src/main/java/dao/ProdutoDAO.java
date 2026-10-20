package dao;

import java.sql.SQLException;

import models.Produto;

public interface ProdutoDAO extends DAO<Produto, String> {
    void atualizar(Produto produto) throws SQLException;
}
