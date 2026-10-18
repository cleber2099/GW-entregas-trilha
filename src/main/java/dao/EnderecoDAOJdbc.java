package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.Endereco;

/**
 * Auxiliar dos DAOs de cliente e entrega: o endereço é sempre gravado
 * junto com eles, na mesma conexão/transação.
 */
class EnderecoDAOJdbc {
    private static final String INSERT =
            "INSERT INTO endereco (rua, numero, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_POR_ID =
            "SELECT rua, numero, bairro, cidade, estado, cep FROM endereco WHERE id = ?";

    int salvar(Connection conn, Endereco endereco) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, endereco);
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                chaves.next();
                return chaves.getInt(1);
            }
        }
    }

    Endereco buscarPorId(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private void preencher(PreparedStatement ps, Endereco endereco) throws SQLException {
        ps.setString(1, endereco.getRua());
        ps.setString(2, endereco.getNumero());
        ps.setString(3, endereco.getBairro());
        ps.setString(4, endereco.getCidade());
        ps.setString(5, endereco.getEstado());
        ps.setString(6, endereco.getCep());
    }

    private Endereco mapear(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getString("rua"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("cep"),
                rs.getString("numero"),
                rs.getString("bairro"));
    }
}
