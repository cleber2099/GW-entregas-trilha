package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionFactory;
import models.Cliente;
import models.Endereco;

public class ClienteDAOJdbc implements ClienteDAO {
    private static final String COLUNAS =
            "c.nome, c.telefone, c.documento, e.rua, e.numero, e.bairro, e.cidade, e.estado, e.cep "
            + "FROM cliente c JOIN endereco e ON e.id = c.endereco_id ";

    private static final String INSERT =
            "INSERT INTO cliente (nome, telefone, documento, endereco_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_POR_DOCUMENTO =
            "SELECT " + COLUNAS + "WHERE c.documento = ?";
    private static final String SELECT_POR_ID =
            "SELECT " + COLUNAS + "WHERE c.id = ?";
    private static final String SELECT_TODOS =
            "SELECT " + COLUNAS + "ORDER BY c.nome";
    private static final String SELECT_ID_POR_DOCUMENTO =
            "SELECT id, endereco_id FROM cliente WHERE documento = ?";
    private static final String UPDATE =
            "UPDATE cliente SET nome = ?, telefone = ? WHERE documento = ?";
    private static final String UPDATE_ENDERECO =
            "UPDATE endereco SET rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? WHERE id = ?";
    private static final String DELETE =
            "DELETE FROM cliente WHERE documento = ?";
    private static final String DELETE_ENDERECO =
            "DELETE FROM endereco WHERE id = ?";

    private final EnderecoDAOJdbc enderecoDAO = new EnderecoDAOJdbc();

    @Override
    public void salvar(Cliente cliente) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int enderecoId = enderecoDAO.salvar(conn, cliente.getEndereco());
                try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
                    ps.setString(1, cliente.getNome());
                    ps.setString(2, cliente.getTelefone());
                    ps.setString(3, cliente.getDocumento());
                    ps.setInt(4, enderecoId);
                    ps.executeUpdate();
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
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_DOCUMENTO)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        }
        return clientes;
    }

    @Override
    public boolean atualizar(Cliente cliente) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int[] ids = buscarIds(conn, cliente.getDocumento());
                if (ids == null) {
                    conn.rollback();
                    return false;
                }
                try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
                    ps.setString(1, cliente.getNome());
                    ps.setString(2, cliente.getTelefone());
                    ps.setString(3, cliente.getDocumento());
                    ps.executeUpdate();
                }
                Endereco endereco = cliente.getEndereco();
                try (PreparedStatement ps = conn.prepareStatement(UPDATE_ENDERECO)) {
                    ps.setString(1, endereco.getRua());
                    ps.setString(2, endereco.getNumero());
                    ps.setString(3, endereco.getBairro());
                    ps.setString(4, endereco.getCidade());
                    ps.setString(5, endereco.getEstado());
                    ps.setString(6, endereco.getCep());
                    ps.setInt(7, ids[1]);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public boolean remover(String documento) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int[] ids = buscarIds(conn, documento);
                if (ids == null) {
                    conn.rollback();
                    return false;
                }
                try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
                    ps.setString(1, documento);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(DELETE_ENDERECO)) {
                    ps.setInt(1, ids[1]);
                    ps.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    int[] buscarIds(Connection conn, String documento) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ID_POR_DOCUMENTO)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? new int[] {rs.getInt("id"), rs.getInt("endereco_id")} : null;
            }
        }
    }

    Cliente buscarPorId(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("rua"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("cep"),
                rs.getString("numero"),
                rs.getString("bairro"));
        return new Cliente(
                rs.getString("nome"),
                endereco,
                rs.getString("telefone"),
                rs.getString("documento"));
    }
}
