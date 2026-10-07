package dao;

import java.sql.SQLException;
import java.util.List;

import models.Produto;

public interface ProdutoDAO {
    void inserir(Produto produto) throws SQLException;

    Produto buscarPorCodigo(String codigo) throws SQLException;

    List<Produto> listar() throws SQLException;

    boolean atualizar(Produto produto) throws SQLException;

    boolean remover(String codigo) throws SQLException;
}
