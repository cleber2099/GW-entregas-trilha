package service;

import java.sql.SQLException;
import java.util.List;

import dao.DAO;

/**
 * Operações comuns aos services. O tipo D guarda o DAO específico,
 * para que as subclasses acessem métodos próprios (ex.: atualizar).
 */
public abstract class ServiceBase<T, K, D extends DAO<T, K>> {
    protected final D dao;
    private final String nome;
    private final String nomePlural;
    private final String mensagemNaoEncontrado;

    protected ServiceBase(D dao, String nome, String nomePlural, String mensagemNaoEncontrado) {
        this.dao = dao;
        this.nome = nome;
        this.nomePlural = nomePlural;
        this.mensagemNaoEncontrado = mensagemNaoEncontrado;
    }

    public List<T> listar() {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao listar " + nomePlural + ".", e);
        }
    }

    protected T buscarPorChave(K chave) {
        try {
            return dao.buscarPorChave(chave);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar " + nome + ".", e);
        }
    }

    protected T buscarExistente(K chave) {
        T entidade = buscarPorChave(chave);
        if (entidade == null) {
            throw new IllegalArgumentException(mensagemNaoEncontrado);
        }
        return entidade;
    }

    protected void cadastrarNovo(T entidade, K chave, String mensagemDuplicado) {
        try {
            if (dao.buscarPorChave(chave) != null) {
                throw new IllegalArgumentException(mensagemDuplicado);
            }
            dao.salvar(entidade);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao cadastrar " + nome + ".", e);
        }
    }

    protected void removerPorChave(K chave, String mensagemErro) {
        try {
            if (!dao.remover(chave)) {
                throw new IllegalArgumentException(mensagemNaoEncontrado);
            }
        } catch (SQLException e) {
            throw new ServiceException(mensagemErro, e);
        }
    }
}
