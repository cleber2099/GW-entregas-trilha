package dao;

import java.sql.SQLException;

import models.entrega.Entrega;

public interface EntregaDAO extends DAO<Entrega, String> {
    boolean atualizarStatus(Entrega entrega) throws SQLException;

    boolean atualizarItens(Entrega entrega) throws SQLException;
}
