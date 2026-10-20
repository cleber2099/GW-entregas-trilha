package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionFactory;
import models.Cliente;
import models.Endereco;

public class ClienteDAOJdbc implements ClienteDAO {
    private static final String SELECT =
            "SELECT id, nome, telefone, documento FROM cliente ";

    private static final String INSERT =
            "INSERT INTO cliente (nome, telefone, documento) VALUES (?, ?, ?)";
    private static final String SELECT_POR_DOCUMENTO =
            SELECT + "WHERE documento = ?";
    private static final String SELECT_POR_ID =
            SELECT + "WHERE id = ?";
    private static final String SELECT_POR_ENDERECO =
            SELECT + "WHERE id = (SELECT cliente_id FROM endereco WHERE id = ?)";
    private static final String SELECT_TODOS =
            SELECT + "ORDER BY nome";
    private static final String UPDATE =
            "UPDATE cliente SET nome = ?, telefone = ? WHERE documento = ?";
    private static final String DELETE =
            "DELETE FROM cliente WHERE documento = ?";

    private final EnderecoDAOJdbc enderecoDAO = new EnderecoDAOJdbc();

    @Override
    public void salvar(Cliente cliente) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int clienteId;
                try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, cliente.getNome());
                    ps.setString(2, cliente.getTelefone());
                    ps.setString(3, cliente.getDocumento());
                    ps.executeUpdate();
                    try (ResultSet chaves = ps.getGeneratedKeys()) {
                        chaves.next();
                        clienteId = chaves.getInt(1);
                    }
                }
                for (Endereco endereco : cliente.getEnderecos()) {
                    enderecoDAO.salvar(conn, clienteId, endereco);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public Cliente buscarPorChave(String documento) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return buscarUm(conn, SELECT_POR_DOCUMENTO, documento);
        }
    }

    @Override
    public Cliente buscarPorEndereco(int enderecoId) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            return buscarUm(conn, SELECT_POR_ENDERECO, enderecoId);
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(conn, rs));
            }
        }
        return clientes;
    }

    @Override
    public void atualizar(Cliente cliente) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getTelefone());
            ps.setString(3, cliente.getDocumento());
            ps.executeUpdate();
        }
    }

    @Override
    public boolean remover(String documento) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setString(1, documento);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public void adicionarEndereco(String documento, Endereco endereco) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            Integer clienteId = buscarId(conn, documento);
            if (clienteId == null) {
                throw new SQLException("Cliente não cadastrado: " + documento);
            }
            enderecoDAO.salvar(conn, clienteId, endereco);
        }
    }

    @Override
    public void removerEndereco(int enderecoId) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            enderecoDAO.remover(conn, enderecoId);
        }
    }

    Cliente buscarPorId(Connection conn, int id) throws SQLException {
        return buscarUm(conn, SELECT_POR_ID, id);
    }

    private Integer buscarId(Connection conn, String documento) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM cliente WHERE documento = ?")) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id") : null;
            }
        }
    }

    private Cliente buscarUm(Connection conn, String sql, Object parametro) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(conn, rs) : null;
            }
        }
    }

    private Cliente mapear(Connection conn, ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente(
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("documento"));
        for (Endereco endereco : enderecoDAO.listarPorCliente(conn, rs.getInt("id"))) {
            cliente.adicionarEndereco(endereco);
        }
        return cliente;
    }
}
