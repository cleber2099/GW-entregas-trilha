package models;

import util.Validador;

public class Produto {
    private String codigo;
    private String nome;
    private double peso;
    private double volume;
    private double valor;

    public Produto(String codigo, String nome, double peso, double volume, double valor) {
        setCodigo(codigo);
        setNome(nome);
        setPeso(peso);
        setVolume(volume);
        setValor(valor);
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

    @Override
    public String toString() {
        return "Produto: " + nome + " (" + codigo + ") | Peso: " + String.format("%.2f", peso)
                + " kg | Volume: " + String.format("%.3f", volume)
                + " m³ | Valor: R$" + String.format("%.2f", valor);
    }
}
