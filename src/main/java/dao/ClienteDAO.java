package dao;

import java.sql.SQLException;

import models.Cliente;
import models.Endereco;

public interface ClienteDAO extends DAO<Cliente, String> {
    void atualizar(Cliente cliente) throws SQLException;

    void adicionarEndereco(String documento, Endereco endereco) throws SQLException;

    void removerEndereco(int enderecoId) throws SQLException;

    Cliente buscarPorEndereco(int enderecoId) throws SQLException;
}
