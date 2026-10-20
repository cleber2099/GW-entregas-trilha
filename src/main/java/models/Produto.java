package models;

import util.Validador;

public class Produto {
    private String codigo;
    private String nome;
    private double peso;
    private double volume;
    private double valor;
    private int estoque;

    public Produto(String codigo, String nome, double peso, double volume, double valor, int estoque) {
        setCodigo(codigo);
        setNome(nome);
        setPeso(peso);
        setVolume(volume);
        setValor(valor);
        setEstoque(estoque);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        Validador.exigirTexto(codigo, "O código do produto é obrigatório.");
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        Validador.exigirTexto(nome, "O nome do produto é obrigatório.");
        this.nome = nome;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        Validador.exigirPositivo(peso, "O peso deve ser maior que zero.");
        this.peso = peso;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        Validador.exigirPositivo(volume, "O volume deve ser maior que zero.");
        this.volume = volume;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        Validador.exigirPositivo(valor, "O valor deve ser maior que zero.");
        this.valor = valor;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        Validador.exigirMinimo(estoque, 0, "O estoque não pode ser negativo.");
        this.estoque = estoque;
    }

    public void retirarEstoque(int quantidade) {
        if (quantidade > estoque) {
            throw new IllegalArgumentException("Estoque insuficiente: há " + estoque + " unidade(s) de " + nome + ".");
        }
        estoque -= quantidade;
    }

    @Override
    public String toString() {
        return nome + " (" + codigo + ")";
    }
}
