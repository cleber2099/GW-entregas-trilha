package models;

public class ItemEntrega {
 
    private Produto produto;
    private int quantidade;
 
    public ItemEntrega(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.produto = produto;
        this.quantidade = quantidade;
    }
 
    public Produto getProduto() {
        return produto;
    }
 
    public int getQuantidade() {
        return quantidade;
    }
 
    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
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