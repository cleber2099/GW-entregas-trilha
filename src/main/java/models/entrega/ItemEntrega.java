package models.entrega;

import models.Produto;
import util.Validador;

class ItemEntrega {

    private Produto produto;
    private int quantidade;

    ItemEntrega(Produto produto, int quantidade) {
        Validador.exigirNaoNulo(produto, "O produto do item é obrigatório.");
        setQuantidade(quantidade);
        this.produto = produto;
    }

    Produto getProduto() {
        return produto;
    }

    int getQuantidade() {
        return quantidade;
    }

    void setQuantidade(int quantidade) {
        Validador.exigirMinimo(quantidade, 1, "A quantidade deve ser maior que zero.");
        this.quantidade = quantidade;
    }

    double calcularValor() {
        return produto.getValor() * quantidade;
    }

    double calcularPeso() {
        return produto.getPeso() * quantidade;
    }

    double calcularVolume() {
        return produto.getVolume() * quantidade;
    }

    @Override
    public String toString() {
        return produto.getNome() + " x" + quantidade + " | " + String.format("%.2f", calcularPeso())
                + " kg | " + String.format("%.3f", calcularVolume())
                + " m³ | R$" + String.format("%.2f", calcularValor());
    }
}
