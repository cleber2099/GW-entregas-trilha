package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Cliente;
import models.Endereco;

public class ClienteController {

    private List<Cliente> clientes = new ArrayList<>();
    private Scanner scanner;

    public ClienteController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Clientes ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Buscar por CPF");
            System.out.println("4 - Editar");
            System.out.println("5 - Remover");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1: cadastrar(); break;
                case 2: listar(); break;
                case 3: buscarInterativo(); break;
                case 4: editar(); break;
                case 5: remover(); break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n-- Cadastro de Cliente --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        Endereco endereco = lerEndereco();

        clientes.add(new Cliente(nome, endereco, telefone, cpf));
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void listar() {
        System.out.println("\n-- Lista de Clientes --");
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente c : clientes) {
            System.out.println(c);
        }
    }

    public Cliente buscarPorCpf(String cpf) {
        for (Cliente c : clientes) {
            if (c.getCpf().equalsIgnoreCase(cpf)) {
                return c;
            }
        }
        return null;
    }

    private void buscarInterativo() {
        System.out.print("Informe o CPF: ");
        Cliente cliente = buscarPorCpf(scanner.nextLine());
        System.out.println(cliente != null ? cliente : "Cliente não encontrado.");
    }

    private void editar() {
        System.out.print("CPF do cliente a editar: ");
        Cliente cliente = buscarPorCpf(scanner.nextLine());
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        System.out.print("Novo nome (" + cliente.getNome() + "): ");
        cliente.setNome(scanner.nextLine());
        System.out.print("Novo telefone (" + cliente.getTelefone() + "): ");
        cliente.setTelefone(scanner.nextLine());
        System.out.print("Deseja atualizar o endereço? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            cliente.setEndereco(lerEndereco());
        }
        System.out.println("Cliente atualizado com sucesso!");
    }

    private void remover() {
        System.out.print("CPF do cliente a remover: ");
        Cliente cliente = buscarPorCpf(scanner.nextLine());
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        clientes.remove(cliente);
        System.out.println("Cliente removido com sucesso!");
    }

    private Endereco lerEndereco() {
        System.out.print("Rua: ");
        String rua = scanner.nextLine();
        System.out.print("Número: ");
        String numero = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();
        System.out.print("CEP: ");
        String cep = scanner.nextLine();
        return new Endereco(rua, cidade, estado, cep, numero, bairro);
    }
}