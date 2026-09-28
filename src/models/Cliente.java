package models;

import util.Validador;

public class Cliente {
    private String nome;
    private String telefone;
    private String cpf;
    private Endereco endereco;
    public Cliente(String nome, Endereco endereco, String telefone, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        setTelefone(telefone);
        setCpf(cpf);
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        Validador.exigirTexto(cpf, "O CPF do cliente é obrigatório.");
        String digitos = cpf.replaceAll("\\D", "");
        if (digitos.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        }
        this.cpf = cpf;
    }

     public String exibirDados() {
        return "Cliente: " + nome + " | CPF: " + cpf + " | E-mail: "
                + " | Telefone: " + telefone + " | Endereço: " + endereco.formatar();
    }
 
    @Override
    public String toString() {
        return exibirDados();
    }


    
}
