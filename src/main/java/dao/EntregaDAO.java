package dao;

import java.sql.SQLException;

import models.Produto;
import models.entrega.Entrega;

public interface EntregaDAO extends DAO<Entrega, String> {
    void adicionarItem(String codigoEntrega, Produto produto, int quantidade) throws SQLException;

    void removerItem(String codigoEntrega, String codigoProduto) throws SQLException;

    void atualizarStatus(Entrega entrega) throws SQLException;

    void cancelar(String codigoEntrega) throws SQLException;

    @Override
    boolean remover(String codigoEntrega) throws SQLException;
}
