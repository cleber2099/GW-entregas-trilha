package service;

import java.sql.SQLException;

import dao.DAOFactory;
import dao.EntregaDAO;
import models.entrega.Entrega;
import util.Validador;

public class EntregaService extends ServiceBase<Entrega, String, EntregaDAO> {

    public EntregaService() {
        this(DAOFactory.criarEntregaDAO());
    }

    public EntregaService(EntregaDAO dao) {
        super(dao, "entrega", "entregas", "Entrega não encontrada.");
    }

    public void cadastrar(Entrega entrega) {
        if (entrega.estaVazia()) {
            throw new IllegalArgumentException("A entrega precisa ter ao menos um item.");
        }
        cadastrarNovo(entrega, entrega.getCodigo(), "Já existe uma entrega com esse código.");
    }

    public Entrega buscarPorCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código da entrega é obrigatório.");
        return buscarPorChave(codigo);
    }

    public void atualizarItens(Entrega entrega) {
        try {
            dao.atualizarItens(entrega);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar os itens da entrega.", e);
        }
    }

    public void atualizarStatus(Entrega entrega) {
        try {
            dao.atualizarStatus(entrega);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar o status da entrega.", e);
        }
    }

    public void remover(String codigo) {
        removerPorChave(codigo, "Erro ao remover entrega.");
    }
}
