package dao;

import java.sql.SQLException;

import models.Produto;

public interface ProdutoDAO extends DAO<Produto, String> {
    boolean atualizar(Produto produto) throws SQLException;
}
