package service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import dao.EntregaDAO;
import models.Cliente;
import models.Produto;
import models.entrega.Entrega;
import util.Validador;

public class EntregaService extends ServiceBase<Entrega, EntregaDAO> {
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    public EntregaService() {
        this(new EntregaDAO(), new ClienteService(), new ProdutoService());
    }

    public EntregaService(EntregaDAO dao, ClienteService clienteService, ProdutoService produtoService) {
        super(dao, "entrega", "Entrega não encontrada.");
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public Entrega cadastrar(int origemId, int destinoId) {
        Cliente remetente = donoDoEndereco(origemId);
        Cliente destinatario = donoDoEndereco(destinoId);
        String codigo = "ENT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Entrega entrega = new Entrega(codigo, remetente, destinatario,
                remetente.buscarEndereco(origemId), destinatario.buscarEndereco(destinoId));
        cadastrarNovo(entrega, entrega.getCodigo(), "Já existe uma entrega com esse código.");
        return entrega;
    }

    public List<Entrega> buscar(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listar();
        }
        String texto = termo.trim().toLowerCase();
        String digitos = termo.replaceAll("\\D", "");
        return listar().stream()
                .filter(e -> e.getCodigo().toLowerCase().contains(texto)
                        || e.getStatus().getDescricao().toLowerCase().contains(texto)
                        || e.getRemetente().getNome().toLowerCase().contains(texto)
                        || e.getDestinatario().getNome().toLowerCase().contains(texto)
                        || (!digitos.isEmpty() && (e.getRemetente().getDocumento().contains(digitos)
                                || e.getDestinatario().getDocumento().contains(digitos))))
                .collect(Collectors.toList());
    }

    public Entrega buscarPorCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código da entrega é obrigatório.");
        return buscarPorChave(codigo);
    }

    public void adicionarItem(String codigo, String codigoProduto, int quantidade) {
        Entrega entrega = buscarExistente(codigo);
        Produto produto = produtoService.buscarPorCodigo(codigoProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        entrega.adicionarItem(produto, quantidade);
        produto.retirarEstoque(quantidade);
        try {
            dao.adicionarItem(entrega.getCodigo(), produto, quantidade);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao adicionar o produto.", e);
        }
    }

    public void removerItem(String codigo, String codigoProduto) {
        Entrega entrega = buscarExistente(codigo);
        if (!entrega.removerItem(codigoProduto)) {
            throw new IllegalArgumentException("Produto não está nesta entrega.");
        }
        try {
            dao.removerItem(entrega.getCodigo(), codigoProduto);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao remover o produto.", e);
        }
    }

    public void iniciarTransporte(String codigo) {
        Entrega entrega = buscarExistente(codigo);
        entrega.iniciarTransporte();
        salvarStatus(entrega);
    }

    public void confirmarEntrega(String codigo) {
        Entrega entrega = buscarExistente(codigo);
        entrega.confirmarEntrega();
        salvarStatus(entrega);
    }

    public void cancelar(String codigo) {
        Entrega entrega = buscarExistente(codigo);
        entrega.cancelar();
        try {
            dao.cancelar(entrega.getCodigo());
        } catch (SQLException e) {
            throw new ServiceException("Erro ao cancelar a entrega.", e);
        }
    }

    public void remover(String codigo) {
        Entrega entrega = buscarExistente(codigo);
        if (!entrega.podeExcluir()) {
            throw new IllegalStateException("Só é possível excluir uma entrega pendente. Cancele-a, se for o caso.");
        }
        removerPorChave(entrega.getCodigo(), "Erro ao remover entrega.");
    }

    private Cliente donoDoEndereco(int enderecoId) {
        Cliente cliente = clienteService.buscarPorEndereco(enderecoId);
        if (cliente == null) {
            throw new IllegalArgumentException("Endereço não encontrado.");
        }
        return cliente;
    }

    private void salvarStatus(Entrega entrega) {
        try {
            dao.atualizar(entrega);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar o status da entrega.", e);
        }
    }
}
