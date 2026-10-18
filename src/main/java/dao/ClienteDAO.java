package dao;

import java.sql.SQLException;

import models.Cliente;

public interface ClienteDAO extends DAO<Cliente, String> {
    boolean atualizar(Cliente cliente) throws SQLException;
}
