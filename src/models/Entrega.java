package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Validador;

public class Entrega {

    public enum Status {
        PENDENTE,
        EM_TRANSITO,
        ENTREGUE,
        CANCELADO
    }

    private String codigo;
    private Cliente cliente;
    private Endereco enderecoDestino;
    private Status status;
    private List<ItemEntrega> itens = new ArrayList<>();

    public Entrega(String codigo, Cliente cliente) {
        this(codigo, cliente, cliente != null ? cliente.getEndereco() : null);
    }

    public Entrega(String codigo, Cliente cliente, Endereco enderecoDestino) {
        this.status = Status.PENDENTE;
        setCodigo(codigo);
        setCliente(cliente);
        setEnderecoDestino(enderecoDestino);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código da entrega é obrigatório.");
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        Validador.exigirNaoNulo(cliente, "O cliente é obrigatório.");
        if (!podeAlterar()) {
            return;
        }
        this.cliente = cliente;
    }

    public Endereco getEnderecoDestino() {
        return enderecoDestino;
    }

    public void setEnderecoDestino(Endereco enderecoDestino) {
        Validador.exigirNaoNulo(enderecoDestino, "O endereço de destino é obrigatório.");
        if (!podeAlterar()) {
            return;
        }
        this.enderecoDestino = enderecoDestino;
    }

    public Status getStatus() {
        return status;
    }

    public List<ItemEntrega> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void adicionarItem(Produto produto, int quantidade) {
        Validador.exigirNaoNulo(produto, "O produto é obrigatório.");
        if (!podeAlterar()) {
            return;
        }

        ItemEntrega existente = buscarItem(produto.getCodigo());
        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + quantidade);
        } else {
            itens.add(new ItemEntrega(produto, quantidade));
        }
    }

    public boolean removerItem(String codigoProduto) {
        if (!podeAlterar()) {
            return false;
        }

        ItemEntrega item = buscarItem(codigoProduto);
        if (item == null) {
            return false;
        }
        itens.remove(item);
        return true;
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemEntrega item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    public void iniciarTransito() {
        if (status != Status.PENDENTE) {
            System.out.println("Erro: Só é possível iniciar o trânsito de uma entrega pendente (status atual: " + status + ").");
            return;
        }
        if (itens.isEmpty()) {
            System.out.println("Erro: A entrega precisa ter ao menos um item para sair para entrega.");
            return;
        }
        status = Status.EM_TRANSITO;
    }

    public void confirmarEntrega() {
        if (status != Status.EM_TRANSITO) {
            System.out.println("Erro: Só é possível confirmar uma entrega em trânsito (status atual: " + status + ").");
            return;
        }
        status = Status.ENTREGUE;
    }

    public void cancelar() {
        if (status == Status.ENTREGUE || status == Status.CANCELADO) {
            System.out.println("Erro: Não é possível cancelar uma entrega " + status + ".");
            return;
        }
        status = Status.CANCELADO;
    }

    private ItemEntrega buscarItem(String codigoProduto) {
        for (ItemEntrega item : itens) {
            if (item.getProduto().getCodigo().equalsIgnoreCase(codigoProduto)) {
                return item;
            }
        }
        return null;
    }

    private boolean podeAlterar() {
        if (status != Status.PENDENTE) {
            System.out.println("Erro: A entrega não pode ser alterada no status " + status + ".");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String nomeCliente = cliente != null ? cliente.getNome() : "(sem cliente)";
        String destino = enderecoDestino != null ? enderecoDestino.formatar() : "(sem endereço)";

        String texto = "Entrega " + codigo + " [" + status + "]\n";
        texto += "  Cliente: " + nomeCliente + "\n";
        texto += "  Destino: " + destino + "\n";

        if (itens.isEmpty()) {
            texto += "  Itens: (nenhum)\n";
        } else {
            texto += "  Itens:\n";
            for (ItemEntrega item : itens) {
                texto += "    - " + item + "\n";
            }
        }

        texto += "  Total: R$" + String.format("%.2f", calcularTotal());
        return texto;
    }
}