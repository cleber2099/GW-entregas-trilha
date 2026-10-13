package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionFactory;
import models.Produto;

public class ProdutoDAOJdbc implements ProdutoDAO {
    private static final String INSERT =
            "INSERT INTO produto (codigo, nome, peso, volume, valor) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_POR_CODIGO =
            "SELECT codigo, nome, peso, volume, valor FROM produto WHERE LOWER(codigo) = LOWER(?)";
    private static final String SELECT_TODOS =
            "SELECT codigo, nome, peso, volume, valor FROM produto ORDER BY nome";
    private static final String UPDATE =
            "UPDATE produto SET nome = ?, peso = ?, volume = ?, valor = ? WHERE LOWER(codigo) = LOWER(?)";
    private static final String DELETE =
            "DELETE FROM produto WHERE LOWER(codigo) = LOWER(?)";

    @Override
    public void inserir(Produto produto) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, produto.getCodigo());
            ps.setString(2, produto.getNome());
            ps.setDouble(3, produto.getPeso());
            ps.setDouble(4, produto.getVolume());
            ps.setDouble(5, produto.getValor());
            ps.executeUpdate();
        }
    }

    @Override
    public Produto buscarPorCodigo(String codigo) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<Produto> listar() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        }
        return produtos;
    }

    @Override
    public boolean atualizar(Produto produto) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPeso());
            ps.setDouble(3, produto.getVolume());
            ps.setDouble(4, produto.getValor());
            ps.setString(5, produto.getCodigo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(String codigo) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getDouble("peso"),
                rs.getDouble("volume"),
                rs.getDouble("valor"));
    }
}
