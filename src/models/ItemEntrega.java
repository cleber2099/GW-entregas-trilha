package models;

import util.Validador;

class ItemEntrega {

    private Produto produto;
    private int quantidade;

    public ItemEntrega(Produto produto, int quantidade) {
        Validador.exigirNaoNulo(produto, "O produto do item é obrigatório.");
        setQuantidade(quantidade);
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        Validador.exigirPositivo(quantidade, "A quantidade deve ser maior que zero.");
        this.quantidade = quantidade;
    }

    public double calcularSubtotal() {
        return produto.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return produto.getNome() + " x" + quantidade + " = R$" + String.format("%.2f", calcularSubtotal());
    }
}