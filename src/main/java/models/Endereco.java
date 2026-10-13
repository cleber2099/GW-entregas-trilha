package models;

import util.Validador;

public class Endereco {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private String bairro;

    public Endereco(String rua, String cidade, String estado, String cep, String numero, String bairro) {
        setRua(rua);
        setCidade(cidade);
        setEstado(estado);
        setCep(cep);
        setNumero(numero);
        setBairro(bairro);
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        Validador.exigirTexto(rua, "A rua é obrigatória.");
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        Validador.exigirTexto(cidade, "A cidade é obrigatória.");
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        Validador.exigirTexto(estado, "O estado é obrigatório.");
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        Validador.exigirTexto(cep, "O CEP é obrigatório.");
        String digitos = cep.replaceAll("\\D", "");
        if (digitos.length() != 8) {
            throw new IllegalArgumentException("O CEP deve conter 8 dígitos.");
        }
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        Validador.exigirTexto(numero, "O número é obrigatório.");
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        Validador.exigirTexto(bairro, "O bairro é obrigatório.");
        this.bairro = bairro;
    }
    public String formatar() {
        return rua + ", " + numero + " - " + bairro + ", " + cidade + "/" + estado + " - CEP: " + cep;
    }
 
    @Override
    public String toString() {
        return formatar();
    }
    

}
