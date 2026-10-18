package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.ConnectionFactory;
import models.Cliente;
import models.Endereco;
import models.Produto;
import models.entrega.Entrega;

public class EntregaDAOJdbc implements EntregaDAO {
    private static final String COLUNAS =
            "SELECT id, codigo, remetente_id, destinatario_id, endereco_origem_id, endereco_destino_id, status "
            + "FROM entrega ";

    private static final String INSERT =
            "INSERT INTO entrega (codigo, remetente_id, destinatario_id, endereco_origem_id, endereco_destino_id, status) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_ITEM =
            "INSERT INTO item_entrega (entrega_id, produto_id, quantidade) "
            + "SELECT ?, id, ? FROM produto WHERE LOWER(codigo) = LOWER(?)";
    private static final String SELECT_POR_CODIGO =
            COLUNAS + "WHERE LOWER(codigo) = LOWER(?)";
    private static final String SELECT_TODAS =
            COLUNAS + "ORDER BY id";
    private static final String SELECT_ITENS =
            "SELECT p.codigo, p.nome, p.peso, p.volume, p.valor, i.quantidade "
            + "FROM item_entrega i JOIN produto p ON p.id = i.produto_id "
            + "WHERE i.entrega_id = ? ORDER BY i.id";
    private static final String UPDATE_STATUS =
            "UPDATE entrega SET status = ? WHERE LOWER(codigo) = LOWER(?)";
    private static final String SELECT_IDS_POR_CODIGO =
            "SELECT id, endereco_origem_id, endereco_destino_id FROM entrega WHERE LOWER(codigo) = LOWER(?)";
    private static final String DELETE_ITENS =
            "DELETE FROM item_entrega WHERE entrega_id = ?";
    private static final String DELETE =
            "DELETE FROM entrega WHERE id = ?";
    private static final String DELETE_ENDERECO =
            "DELETE FROM endereco WHERE id = ?";

    private final ClienteDAOJdbc clienteDAO = new ClienteDAOJdbc();
    private final EnderecoDAOJdbc enderecoDAO = new EnderecoDAOJdbc();

    @Override
    public void salvar(Entrega entrega) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int remetenteId = idDoCliente(conn, entrega.getRemetente());
                int destinatarioId = idDoCliente(conn, entrega.getDestinatario());
                int origemId = enderecoDAO.salvar(conn, entrega.getEnderecoOrigem());
                int destinoId = enderecoDAO.salvar(conn, entrega.getEnderecoDestino());

                int entregaId;
                try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, entrega.getCodigo());
                    ps.setInt(2, remetenteId);
                    ps.setInt(3, destinatarioId);
                    ps.setInt(4, origemId);
                    ps.setInt(5, destinoId);
                    ps.setString(6, paraCodigo(entrega.getStatus()));
                    ps.executeUpdate();
                    try (ResultSet chaves = ps.getGeneratedKeys()) {
                        chaves.next();
                        entregaId = chaves.getInt(1);
                    }
                }

                inserirItens(conn, entregaId, entrega);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public Entrega buscarPorChave(String codigo) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(conn, rs) : null;
            }
        }
    }

    @Override
    public List<Entrega> listar() throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODAS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                entregas.add(mapear(conn, rs));
            }
        }
        return entregas;
    }

    @Override
    public boolean atualizarStatus(Entrega entrega) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)) {
            ps.setString(1, paraCodigo(entrega.getStatus()));
            ps.setString(2, entrega.getCodigo());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean atualizarItens(Entrega entrega) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int[] ids = buscarIds(conn, entrega.getCodigo());
                if (ids == null) {
                    conn.rollback();
                    return false;
                }
                executarPorId(conn, DELETE_ITENS, ids[0]);
                inserirItens(conn, ids[0], entrega);
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Override
    public boolean remover(String codigo) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int[] ids = buscarIds(conn, codigo);
                if (ids == null) {
                    conn.rollback();
                    return false;
                }
                executarPorId(conn, DELETE, ids[0]);
                executarPorId(conn, DELETE_ENDERECO, ids[1]);
                executarPorId(conn, DELETE_ENDERECO, ids[2]);
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private int[] buscarIds(Connection conn, String codigo) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_IDS_POR_CODIGO)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? new int[] {rs.getInt("id"), rs.getInt("endereco_origem_id"), rs.getInt("endereco_destino_id")}
                        : null;
            }
        }
    }

    private void executarPorId(Connection conn, String sql, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private void inserirItens(Connection conn, int entregaId, Entrega entrega) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_ITEM)) {
            for (Map.Entry<Produto, Integer> item : entrega.getItens().entrySet()) {
                ps.setInt(1, entregaId);
                ps.setInt(2, item.getValue());
                ps.setString(3, item.getKey().getCodigo());
                if (ps.executeUpdate() == 0) {
                    throw new SQLException("Produto não cadastrado: " + item.getKey().getCodigo());
                }
            }
        }
    }

    private int idDoCliente(Connection conn, Cliente cliente) throws SQLException {
        int[] ids = clienteDAO.buscarIds(conn, cliente.getDocumento());
        if (ids == null) {
            throw new SQLException("Cliente não cadastrado: " + cliente.getDocumento());
        }
        return ids[0];
    }

    private Entrega mapear(Connection conn, ResultSet rs) throws SQLException {
        Cliente remetente = clienteDAO.buscarPorId(conn, rs.getInt("remetente_id"));
        Cliente destinatario = clienteDAO.buscarPorId(conn, rs.getInt("destinatario_id"));
        Endereco origem = enderecoDAO.buscarPorId(conn, rs.getInt("endereco_origem_id"));
        Endereco destino = enderecoDAO.buscarPorId(conn, rs.getInt("endereco_destino_id"));

        Entrega entrega = new Entrega(rs.getString("codigo"), remetente, destinatario, origem, destino);

        try (PreparedStatement ps = conn.prepareStatement(SELECT_ITENS)) {
            ps.setInt(1, rs.getInt("id"));
            try (ResultSet itens = ps.executeQuery()) {
                while (itens.next()) {
                    Produto produto = new Produto(
                            itens.getString("codigo"),
                            itens.getString("nome"),
                            itens.getDouble("peso"),
                            itens.getDouble("volume"),
                            itens.getDouble("valor"));
                    entrega.adicionarItem(produto, itens.getInt("quantidade"));
                }
            }
        }

        String status = rs.getString("status");
        if ("R".equals(status)) {
            entrega.marcarRealizada();
        } else if ("N".equals(status)) {
            entrega.marcarNaoRealizada();
        }
        return entrega;
    }

    private String paraCodigo(Entrega.Status status) {
        switch (status) {
            case REALIZADA:
                return "R";
            case NAO_REALIZADA:
                return "N";
            default:
                return "P";
        }
    }
}
