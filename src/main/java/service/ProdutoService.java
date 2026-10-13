package service;

import java.sql.SQLException;
import java.util.List;

import dao.DAOFactory;
import dao.ProdutoDAO;
import models.Produto;
import util.Validador;

public class ProdutoService {
    private final ProdutoDAO dao;

    public ProdutoService() {
        this(DAOFactory.criarProdutoDAO());
    }

    public ProdutoService(ProdutoDAO dao) {
        this.dao = dao;
    }

    public Produto cadastrar(String codigo, String nome, double peso, double volume, double valor) {
        Produto produto = new Produto(codigo, nome, peso, volume, valor);
        try {
            if (dao.buscarPorCodigo(produto.getCodigo()) != null) {
                throw new IllegalArgumentException("Já existe um produto com esse código.");
            }
            dao.inserir(produto);
            return produto;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao cadastrar produto.", e);
        }
    }

    public Produto buscarPorCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código do produto é obrigatório.");
        try {
            return dao.buscarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar produto.", e);
        }
    }

    public List<Produto> listar() {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw new ServiceException("Erro ao listar produtos.", e);
        }
    }

    public Produto editar(String codigo, String nome, double peso, double volume, double valor) {
        Produto produto = buscarExistente(codigo);
        produto.setNome(nome);
        produto.setPeso(peso);
        produto.setVolume(volume);
        produto.setValor(valor);
        try {
            dao.atualizar(produto);
            return produto;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar produto.", e);
        }
    }

    public void remover(String codigo) {
        Produto produto = buscarExistente(codigo);
        try {
            dao.remover(produto.getCodigo());
        } catch (SQLException e) {
            throw new ServiceException("Erro ao remover produto (verifique se ele está em alguma entrega).", e);
        }
    }

    private Produto buscarExistente(String codigo) {
        Produto produto = buscarPorCodigo(codigo);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        return produto;
    }
}
