package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionFactory;
import models.Cliente;
import models.Produto;
import models.entrega.Entrega;

public class EntregaDAO implements DAO<Entrega> {
    private static final String SELECT =
            "SELECT e.id, e.codigo, e.status, e.endereco_origem_id, e.endereco_destino_id, "
            + "o.cliente_id AS remetente_id, d.cliente_id AS destinatario_id "
            + "FROM entrega e "
            + "JOIN endereco o ON o.id = e.endereco_origem_id "
            + "JOIN endereco d ON d.id = e.endereco_destino_id ";

    private static final String INSERT =
            "INSERT INTO entrega (codigo, endereco_origem_id, endereco_destino_id, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_POR_CODIGO =
            SELECT + "WHERE LOWER(e.codigo) = LOWER(?)";
    private static final String SELECT_TODAS =
            SELECT + "ORDER BY e.id DESC";
    private static final String SELECT_ID =
            "SELECT id FROM entrega WHERE LOWER(codigo) = LOWER(?)";
    private static final String SELECT_ITENS =
            "SELECT p.codigo, p.nome, p.peso, p.volume, p.estoque, i.valor_unitario, i.quantidade "
            + "FROM item_entrega i JOIN produto p ON p.id = i.produto_id "
            + "WHERE i.entrega_id = ? ORDER BY i.id";
    private static final String UPDATE_STATUS =
            "UPDATE entrega SET status = ? WHERE id = ?";
    private static final String DELETE =
            "DELETE FROM entrega WHERE id = ?";

    private static final String BAIXAR_ESTOQUE =
            "UPDATE produto SET estoque = estoque - ? WHERE LOWER(codigo) = LOWER(?)";
    private static final String INSERIR_OU_SOMAR_ITEM =
            "INSERT INTO item_entrega (entrega_id, produto_id, quantidade, valor_unitario) "
            + "SELECT ?, id, ?, valor FROM produto WHERE LOWER(codigo) = LOWER(?) "
            + "ON CONFLICT (entrega_id, produto_id) "
            + "DO UPDATE SET quantidade = item_entrega.quantidade + EXCLUDED.quantidade";
    private static final String DEVOLVER_ESTOQUE_DO_ITEM =
            "UPDATE produto p SET estoque = p.estoque + i.quantidade FROM item_entrega i "
            + "WHERE i.produto_id = p.id AND i.entrega_id = ? AND LOWER(p.codigo) = LOWER(?)";
    private static final String DELETE_ITEM =
            "DELETE FROM item_entrega i USING produto p "
            + "WHERE p.id = i.produto_id AND i.entrega_id = ? AND LOWER(p.codigo) = LOWER(?)";
    private static final String DEVOLVER_ESTOQUE_DE_TODOS =
            "UPDATE produto p SET estoque = p.estoque + i.quantidade FROM item_entrega i "
            + "WHERE i.produto_id = p.id AND i.entrega_id = ?";

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    public void salvar(Entrega entrega) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            executar(conn, INSERT,
                    entrega.getCodigo(),
                    entrega.getEnderecoOrigem().getId(),
                    entrega.getEnderecoDestino().getId(),
                    entrega.getStatus().name());
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

    public void adicionarItem(String codigoEntrega, Produto produto, int quantidade) throws SQLException {
        emTransacao(codigoEntrega, (conn, entregaId) -> {
            executar(conn, BAIXAR_ESTOQUE, quantidade, produto.getCodigo());
            executar(conn, INSERIR_OU_SOMAR_ITEM, entregaId, quantidade, produto.getCodigo());
        });
    }

    public void removerItem(String codigoEntrega, String codigoProduto) throws SQLException {
        emTransacao(codigoEntrega, (conn, entregaId) -> {
            executar(conn, DEVOLVER_ESTOQUE_DO_ITEM, entregaId, codigoProduto);
            executar(conn, DELETE_ITEM, entregaId, codigoProduto);
        });
    }

    @Override
    public void atualizar(Entrega entrega) throws SQLException {
        emTransacao(entrega.getCodigo(), (conn, entregaId) ->
                executar(conn, UPDATE_STATUS, entrega.getStatus().name(), entregaId));
    }

    public void cancelar(String codigoEntrega) throws SQLException {
        emTransacao(codigoEntrega, (conn, entregaId) -> {
            executar(conn, DEVOLVER_ESTOQUE_DE_TODOS, entregaId);
            executar(conn, UPDATE_STATUS, Entrega.Status.CANCELADA.name(), entregaId);
        });
    }

    @Override
    public boolean remover(String codigoEntrega) throws SQLException {
        if (buscarPorChave(codigoEntrega) == null) {
            return false;
        }
        emTransacao(codigoEntrega, (conn, entregaId) -> {
            executar(conn, DEVOLVER_ESTOQUE_DE_TODOS, entregaId);
            executar(conn, DELETE, entregaId);
        });
        return true;
    }

    private interface Operacao {
        void executar(Connection conn, int entregaId) throws SQLException;
    }

    private void emTransacao(String codigoEntrega, Operacao operacao) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                operacao.executar(conn, buscarId(conn, codigoEntrega));
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private void executar(Connection conn, String sql, Object... parametros) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            ps.executeUpdate();
        }
    }

    private int buscarId(Connection conn, String codigo) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ID)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Entrega não cadastrada: " + codigo);
                }
                return rs.getInt("id");
            }
        }
    }

    private Entrega mapear(Connection conn, ResultSet rs) throws SQLException {
        Cliente remetente = clienteDAO.buscarPorId(conn, rs.getInt("remetente_id"));
        Cliente destinatario = clienteDAO.buscarPorId(conn, rs.getInt("destinatario_id"));

        Entrega entrega = new Entrega(
                rs.getString("codigo"),
                remetente,
                destinatario,
                remetente.buscarEndereco(rs.getInt("endereco_origem_id")),
                destinatario.buscarEndereco(rs.getInt("endereco_destino_id")));

        try (PreparedStatement ps = conn.prepareStatement(SELECT_ITENS)) {
            ps.setInt(1, rs.getInt("id"));
            try (ResultSet itens = ps.executeQuery()) {
                while (itens.next()) {
                    Produto produto = new Produto(
                            itens.getString("codigo"),
                            itens.getString("nome"),
                            itens.getDouble("peso"),
                            itens.getDouble("volume"),
                            itens.getDouble("valor_unitario"),
                            itens.getInt("estoque"));
                    entrega.adicionarItem(produto, itens.getInt("quantidade"));
                }
            }
        }
        entrega.restaurarStatus(Entrega.Status.valueOf(rs.getString("status")));
        return entrega;
    }
}
