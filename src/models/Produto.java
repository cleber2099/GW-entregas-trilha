package models;

public class Produto {
    private String nome;
    private String descricao;
    private double preco;
    private String codigo;

    public Produto(String nome, String descricao, double preco, String codigo) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

      public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.preco = preco;
    }
 
   
    @Override
    public String toString() {
        return "Produto: " + nome + " (" + codigo + ") | Preço: R$" + String.format("%.2f", preco);
    }
    

    
}
