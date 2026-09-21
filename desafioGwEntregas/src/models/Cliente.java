package models;

public class Cliente {
    private String nome;
    private String telefone;
    private String cpf;
    private Endereco endereco;
    public Cliente(String nome, Endereco endereco, String telefone, String cpf) {
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
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
