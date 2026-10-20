package models.entrega;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Cliente;
import models.Endereco;
import models.Produto;
import util.Validador;

public class Entrega {

    public enum Status {
        PENDENTE("Pendente"),
        EM_TRANSITO("Em trânsito"),
        ENTREGUE("Entregue"),
        CANCELADA("Cancelada");

        private final String descricao;

        Status(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private String codigo;
    private Cliente remetente;
    private Cliente destinatario;
    private Endereco enderecoOrigem;
    private Endereco enderecoDestino;
    private Status status = Status.PENDENTE;
    private List<ItemEntrega> itens = new ArrayList<>();

    public Entrega(String codigo, Cliente remetente, Cliente destinatario,
                   Endereco enderecoOrigem, Endereco enderecoDestino) {
        Validador.exigirTexto(codigo, "O código da entrega é obrigatório.");
        Validador.exigirNaoNulo(remetente, "O remetente é obrigatório.");
        Validador.exigirNaoNulo(destinatario, "O destinatário é obrigatório.");
        Validador.exigirNaoNulo(enderecoOrigem, "O endereço de origem é obrigatório.");
        Validador.exigirNaoNulo(enderecoDestino, "O endereço de destino é obrigatório.");
        if (enderecoOrigem.getId() != null && enderecoOrigem.getId().equals(enderecoDestino.getId())) {
            throw new IllegalArgumentException("O endereço de destino não pode ser o mesmo da origem.");
        }
        this.codigo = codigo;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.enderecoOrigem = enderecoOrigem;
        this.enderecoDestino = enderecoDestino;
    }

    public void iniciarTransporte() {
        exigirStatus(Status.PENDENTE, "Só uma entrega pendente pode sair para transporte.");
        if (itens.isEmpty()) {
            throw new IllegalStateException("Adicione ao menos um produto antes de iniciar o transporte.");
        }
        status = Status.EM_TRANSITO;
    }

    public void confirmarEntrega() {
        exigirStatus(Status.EM_TRANSITO, "Só uma entrega em trânsito pode ser confirmada.");
        status = Status.ENTREGUE;
    }

    public void cancelar() {
        if (!podeCancelar()) {
            throw new IllegalStateException("Não é possível cancelar: a entrega está com status "
                    + status.getDescricao() + ".");
        }
        status = Status.CANCELADA;
    }

    public boolean podeAlterarItens() {
        return status == Status.PENDENTE;
    }

    public boolean podeIniciarTransporte() {
        return status == Status.PENDENTE;
    }

    public boolean podeConfirmarEntrega() {
        return status == Status.EM_TRANSITO;
    }

    public boolean podeCancelar() {
        return status == Status.PENDENTE || status == Status.EM_TRANSITO;
    }

    public boolean podeExcluir() {
        return status == Status.PENDENTE;
    }

    public void restaurarStatus(Status status) {
        this.status = status;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        Validador.exigirNaoNulo(produto, "O produto é obrigatório.");
        Validador.exigirMinimo(quantidade, 1, "A quantidade deve ser maior que zero.");
        exigirStatus(Status.PENDENTE, "Só é possível alterar produtos de uma entrega pendente.");

        ItemEntrega existente = buscarItem(produto.getCodigo());
        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + quantidade);
        } else {
            itens.add(new ItemEntrega(produto, quantidade));
        }
    }

    public boolean removerItem(String codigoProduto) {
        exigirStatus(Status.PENDENTE, "Só é possível alterar produtos de uma entrega pendente.");
        ItemEntrega item = buscarItem(codigoProduto);
        return item != null && itens.remove(item);
    }

    public Map<Produto, Integer> getItens() {
        Map<Produto, Integer> resultado = new LinkedHashMap<>();
        for (ItemEntrega item : itens) {
            resultado.put(item.getProduto(), item.getQuantidade());
        }
        return Collections.unmodifiableMap(resultado);
    }

    public boolean estaVazia() {
        return itens.isEmpty();
    }

    public double calcularValorTotal() {
        double total = 0;
        for (ItemEntrega item : itens) {
            total += item.calcularValor();
        }
        return total;
    }

    public double calcularPesoTotal() {
        double total = 0;
        for (ItemEntrega item : itens) {
            total += item.calcularPeso();
        }
        return total;
    }

    public double calcularVolumeTotal() {
        double total = 0;
        for (ItemEntrega item : itens) {
            total += item.calcularVolume();
        }
        return total;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public Endereco getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public Endereco getEnderecoDestino() {
        return enderecoDestino;
    }

    public Status getStatus() {
        return status;
    }

    private void exigirStatus(Status esperado, String mensagem) {
        if (status != esperado) {
            throw new IllegalStateException(mensagem);
        }
    }

    private ItemEntrega buscarItem(String codigoProduto) {
        for (ItemEntrega item : itens) {
            if (item.getProduto().getCodigo().equalsIgnoreCase(codigoProduto)) {
                return item;
            }
        }
        return null;
    }
}
