package models.entrega;

import java.util.ArrayList;
import java.util.List;

import models.Cliente;
import models.Endereco;
import models.Produto;
import util.Validador;

public class Entrega {

    public enum Status {
        PENDENTE,
        REALIZADA,
        NAO_REALIZADA
    }

    private String codigo;
    private Cliente remetente;
    private Cliente destinatario;
    private Endereco enderecoOrigem;
    private Endereco enderecoDestino;
    private Status status;
    private List<ItemEntrega> itens = new ArrayList<>();

    public Entrega(String codigo, Cliente remetente, Cliente destinatario) {
        this(codigo, remetente, destinatario,
                remetente != null ? remetente.getEndereco() : null,
                destinatario != null ? destinatario.getEndereco() : null);
    }

    public Entrega(String codigo, Cliente remetente, Cliente destinatario,
                   Endereco enderecoOrigem, Endereco enderecoDestino) {
        this.status = Status.PENDENTE;
        setCodigo(codigo);
        setRemetente(remetente);
        setDestinatario(destinatario);
        setEnderecoOrigem(enderecoOrigem);
        setEnderecoDestino(enderecoDestino);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código da entrega é obrigatório.");
        this.codigo = codigo;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public void setRemetente(Cliente remetente) {
        Validador.exigirNaoNulo(remetente, "O remetente é obrigatório.");
        if (!podeAlterar()) {
            return;
        }
        this.remetente = remetente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Cliente destinatario) {
        Validador.exigirNaoNulo(destinatario, "O destinatário é obrigatório.");
        if (!podeAlterar()) {
            return;
        }
        this.destinatario = destinatario;
    }

    public Endereco getEnderecoOrigem() {
        return enderecoOrigem;
    }

    public void setEnderecoOrigem(Endereco enderecoOrigem) {
        Validador.exigirNaoNulo(enderecoOrigem, "O endereço de origem é obrigatório.");
        if (!podeAlterar()) {
            return;
        }
        this.enderecoOrigem = enderecoOrigem;
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

    public boolean estaVazia() {
        return itens.isEmpty();
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

    public void marcarRealizada() {
        if (status != Status.PENDENTE) {
            System.out.println("Erro: Só é possível concluir uma entrega pendente (status atual: " + status + ").");
            return;
        }
        if (itens.isEmpty()) {
            System.out.println("Erro: A entrega precisa ter ao menos um produto.");
            return;
        }
        status = Status.REALIZADA;
    }

    public void marcarNaoRealizada() {
        if (status != Status.PENDENTE) {
            System.out.println("Erro: Só é possível marcar como não realizada uma entrega pendente (status atual: " + status + ").");
            return;
        }
        status = Status.NAO_REALIZADA;
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
        String texto = "Entrega " + codigo + " [" + status + "]\n";
        texto += "  Remetente: " + remetente.getNome() + " (" + remetente.getDocumento() + ")\n";
        texto += "  Origem: " + enderecoOrigem.formatar() + "\n";
        texto += "  Destinatário: " + destinatario.getNome() + " (" + destinatario.getDocumento() + ")\n";
        texto += "  Destino: " + enderecoDestino.formatar() + "\n";

        if (itens.isEmpty()) {
            texto += "  Produtos: (nenhum)\n";
        } else {
            texto += "  Produtos:\n";
            for (ItemEntrega item : itens) {
                texto += "    - " + item + "\n";
            }
        }

        texto += "  Peso total: " + String.format("%.2f", calcularPesoTotal()) + " kg\n";
        texto += "  Volume total: " + String.format("%.3f", calcularVolumeTotal()) + " m³\n";
        texto += "  Valor total: R$" + String.format("%.2f", calcularValorTotal());
        return texto;
    }
}
