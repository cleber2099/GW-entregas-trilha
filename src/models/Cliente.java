package models;

import util.Validador;

public class Cliente {
    private String nome;
    private String telefone;
    private String documento;
    private Endereco endereco;
    public Cliente(String nome, Endereco endereco, String telefone, String documento) {
        setNome(nome);
        setEndereco(endereco);
        setTelefone(telefone);
        setDocumento(documento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        Validador.exigirTexto(nome, "O nome do cliente é obrigatório.");
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        Validador.exigirNaoNulo(endereco, "O endereço do cliente é obrigatório.");
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        Validador.exigirTexto(telefone, "O telefone do cliente é obrigatório.");
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        Validador.exigirDocumento(documento, "O CPF/CNPJ é obrigatório.");
        this.documento = documento;
    }

     public String exibirDados() {
        return "Nome: " + nome + " | CPF/CNPJ: " + documento
                + " | Telefone: " + telefone + " | Endereço: " + endereco.formatar();
    }
 
    @Override
    public String toString() {
        return exibirDados();
    }


    
}
