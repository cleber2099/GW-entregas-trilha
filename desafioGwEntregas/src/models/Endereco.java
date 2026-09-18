package models;

public class Endereco {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private String bairro;

    public Endereco(String rua, String cidade, String estado, String cep, String numero, String bairro) {
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.numero = numero;
        this.bairro = bairro;
    }

}
