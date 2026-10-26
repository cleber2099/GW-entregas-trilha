package service;

import java.sql.SQLException;
import java.util.List;

import dao.DAO;

public abstract class ServiceBase<T, D extends DAO<T>> {
    protected final D dao;
    private final String nome;
    private final String mensagemNaoEncontrado;

    protected ServiceBase(D dao, String nome, String mensagemNaoEncontrado) {
        this.dao = dao;
        this.nome = nome;
        this.mensagemNaoEncontrado = mensagemNaoEncontrado;
    }

    public List<T> listar() {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao consultar o banco de dados.", e);
        }
    }

    protected T buscarPorChave(String chave) {
        try {
            return dao.buscarPorChave(chave);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar " + nome + ".", e);
        }
    }

    protected T buscarExistente(String chave) {
        T entidade = buscarPorChave(chave);
        if (entidade == null) {
            throw new IllegalArgumentException(mensagemNaoEncontrado);
        }
        return entidade;
    }

    protected void cadastrarNovo(T entidade, String chave, String mensagemDuplicado) {
        try {
            if (dao.buscarPorChave(chave) != null) {
                throw new IllegalArgumentException(mensagemDuplicado);
            }
            dao.salvar(entidade);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao cadastrar " + nome + ".", e);
        }
    }

    protected void removerPorChave(String chave, String mensagemErro) {
        try {
            if (!dao.remover(chave)) {
                throw new IllegalArgumentException(mensagemNaoEncontrado);
            }
        } catch (SQLException e) {
            throw new ServiceException(mensagemErro, e);
        }
    }
}
