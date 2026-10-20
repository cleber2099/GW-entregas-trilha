package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import util.Validador;

public class Cliente {
    private String nome;
    private String telefone;
    private String documento;
    private List<Endereco> enderecos = new ArrayList<>();

    public Cliente(String nome, String telefone, String documento) {
        setNome(nome);
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
        this.documento = Validador.exigirDocumento(documento, "O CPF/CNPJ é obrigatório.");
    }

    public List<Endereco> getEnderecos() {
        return Collections.unmodifiableList(enderecos);
    }

    public void adicionarEndereco(Endereco endereco) {
        Validador.exigirNaoNulo(endereco, "O endereço é obrigatório.");
        for (Endereco existente : enderecos) {
            if (existente.formatar().equalsIgnoreCase(endereco.formatar())) {
                throw new IllegalArgumentException("Este endereço já está cadastrado para o cliente.");
            }
        }
        enderecos.add(endereco);
    }

    public void removerEndereco(int enderecoId) {
        Endereco endereco = buscarEndereco(enderecoId);
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não encontrado para este cliente.");
        }
        if (enderecos.size() == 1) {
            throw new IllegalArgumentException("O cliente precisa ter ao menos um endereço.");
        }
        enderecos.remove(endereco);
    }

    public Endereco buscarEndereco(int enderecoId) {
        for (Endereco endereco : enderecos) {
            if (endereco.getId() != null && endereco.getId() == enderecoId) {
                return endereco;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nome + " (" + documento + ")";
    }
}
