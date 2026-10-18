package service;

import java.sql.SQLException;

import dao.DAOFactory;
import dao.ProdutoDAO;
import models.Produto;
import util.Validador;

public class ProdutoService extends ServiceBase<Produto, String, ProdutoDAO> {

    public ProdutoService() {
        this(DAOFactory.criarProdutoDAO());
    }

    public ProdutoService(ProdutoDAO dao) {
        super(dao, "produto", "produtos", "Produto não encontrado.");
    }

    public Produto cadastrar(String codigo, String nome, double peso, double volume, double valor) {
        Produto produto = new Produto(codigo, nome, peso, volume, valor);
        cadastrarNovo(produto, produto.getCodigo(), "Já existe um produto com esse código.");
        return produto;
    }

    public Produto buscarPorCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código do produto é obrigatório.");
        return buscarPorChave(codigo);
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
        removerPorChave(codigo, "Erro ao remover produto (verifique se ele está em alguma entrega).");
    }
}
