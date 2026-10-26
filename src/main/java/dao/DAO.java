package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    void salvar(T entidade) throws SQLException;

    void atualizar(T entidade) throws SQLException;

    T buscarPorChave(String chave) throws SQLException;

    List<T> listar() throws SQLException;

    boolean remover(String chave) throws SQLException;
}
