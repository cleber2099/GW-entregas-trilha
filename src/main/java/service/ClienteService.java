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
        super(dao, "cliente", "clientes", "Cliente não encontrado.");
    }

    public Cliente cadastrar(String nome, Endereco endereco, String telefone, String documento) {
        Cliente cliente = new Cliente(nome, endereco, telefone, documento);
        cadastrarNovo(cliente, cliente.getDocumento(), "Já existe um cliente com esse CPF/CNPJ.");
        return cliente;
    }

    public Cliente buscarPorDocumento(String documento) {
        Validador.exigirTexto(documento, "O CPF/CNPJ é obrigatório.");
        return buscarPorChave(somenteDigitos(documento));
    }

    public Cliente editar(String documento, String nome, String telefone, Endereco endereco) {
        Cliente cliente = buscarExistente(somenteDigitos(documento));
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        if (endereco != null) {
            cliente.setEndereco(endereco);
        }
        try {
            dao.atualizar(cliente);
            return cliente;
        } catch (SQLException e) {
            throw new ServiceException("Erro ao atualizar cliente.", e);
        }
    }

    public void remover(String documento) {
        removerPorChave(somenteDigitos(documento), "Erro ao remover cliente (verifique se ele está em alguma entrega).");
    }

    private String somenteDigitos(String documento) {
        return documento.replaceAll("\\D", "");
    }
}
