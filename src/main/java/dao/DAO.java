package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, K> {
    void salvar(T entidade) throws SQLException;

    T buscarPorChave(K chave) throws SQLException;

    List<T> listar() throws SQLException;

    boolean remover(K chave) throws SQLException;
}
