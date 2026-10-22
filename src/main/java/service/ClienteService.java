package service;

import java.sql.SQLException;

import dao.ClienteDAO;
import dao.DAOFactory;
import models.Cliente;
import models.Endereco;
import util.Validador;

public class ClienteService extends ServiceBase<Cliente, String, ClienteDAO> {

    public ClienteService() {
        this(DAOFactory.criarClienteDAO());
    }

    public ClienteService(ClienteDAO dao) {
        super(dao, "cliente", "Cliente não encontrado.");
    }

    public Cliente cadastrar(String nome, String telefone, String documento, Endereco endereco) {
        Cliente cliente = new Cliente(nome, telefone, documento);
        cliente.adicionarEndereco(endereco);
        cadastrarNovo(cliente, cliente.getDocumento(), "Já existe um cliente com esse CPF/CNPJ.");
        return cliente;
    }

    public Cliente buscarPorDocumento(String documento) {
        Validador.exigirTexto(documento, "O CPF/CNPJ é obrigatório.");
        return buscarPorChave(somenteDigitos(documento));
    }

    public Cliente editar(String documento, String nome, String telefone) {
        Cliente cliente = buscarExistente(somenteDigitos(documento));
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        try {
            dao.atualizar(cliente);
            return cliente;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar cliente.", e);
        }
    }

    public void remover(String documento) {
        removerPorChave(somenteDigitos(documento), "Não foi possível excluir: o cliente está em alguma entrega.");
    }

    public void adicionarEndereco(String documento, Endereco endereco) {
        Cliente cliente = buscarExistente(somenteDigitos(documento));
        cliente.adicionarEndereco(endereco);
        try {
            dao.adicionarEndereco(cliente.getDocumento(), endereco);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao adicionar endereço.", e);
        }
    }

    public void editarEndereco(String documento, int enderecoId, Endereco endereco) {
        Cliente cliente = buscarExistente(somenteDigitos(documento));
        Endereco atualizado = cliente.atualizarEndereco(enderecoId, endereco);
        try {
            dao.atualizarEndereco(atualizado);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar endereço.", e);
        }
    }

    public void removerEndereco(String documento, int enderecoId) {
        Cliente cliente = buscarExistente(somenteDigitos(documento));
        cliente.removerEndereco(enderecoId);
        try {
            dao.removerEndereco(enderecoId);
        } catch (SQLException e) {
            throw new ServiceException("Não foi possível remover: o endereço está em alguma entrega.", e);
        }
    }

    public Cliente buscarPorEndereco(int enderecoId) {
        try {
            return dao.buscarPorEndereco(enderecoId);
        } catch (SQLException e) {
            throw new ServiceException("Erro ao buscar cliente.", e);
        }
    }

    private String somenteDigitos(String documento) {
        return documento.replaceAll("\\D", "");
    }
}
