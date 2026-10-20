package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Endereco;

class EnderecoDAOJdbc {
    private static final String INSERT =
            "INSERT INTO endereco (cliente_id, rua, numero, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_POR_CLIENTE =
            "SELECT id, rua, numero, bairro, cidade, estado, cep FROM endereco WHERE cliente_id = ? ORDER BY id";
    private static final String DELETE =
            "DELETE FROM endereco WHERE id = ?";

    int salvar(Connection conn, int clienteId, Endereco endereco) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, clienteId);
            ps.setString(2, endereco.getRua());
            ps.setString(3, endereco.getNumero());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getCidade());
            ps.setString(6, endereco.getEstado());
            ps.setString(7, endereco.getCep());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                chaves.next();
                endereco.setId(chaves.getInt(1));
                return endereco.getId();
            }
        }
    }

    List<Endereco> listarPorCliente(Connection conn, int clienteId) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_POR_CLIENTE)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(mapear(rs));
                }
            }
        }
        return enderecos;
    }

    void remover(Connection conn, int enderecoId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, enderecoId);
            ps.executeUpdate();
        }
    }

    private Endereco mapear(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("rua"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("cep"),
                rs.getString("numero"),
                rs.getString("bairro"));
        endereco.setId(rs.getInt("id"));
        return endereco;
    }
}
