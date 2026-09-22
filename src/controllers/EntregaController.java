package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Cliente;
import models.Endereco;
import models.Entrega;
import models.Produto;

public class EntregaController {

    private List<Entrega> entregas = new ArrayList<>();
    private Scanner scanner;
    private ClienteController clienteController;
    private ProdutoController produtoController;

    public EntregaController(Scanner scanner, ClienteController clienteController, ProdutoController produtoController) {
        this.scanner = scanner;
        this.clienteController = clienteController;
        this.produtoController = produtoController;
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- Entregas ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todas");
            System.out.println("3 - Buscar por código");
            System.out.println("4 - Adicionar item");
            System.out.println("5 - Remover item");
            System.out.println("6 - Atualizar status");
            System.out.println("7 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1: cadastrar(); break;
                case 2: listar(); break;
                case 3: buscarInterativo(); break;
                case 4: adicionarItem(); break;
                case 5: removerItem(); break;
                case 6: atualizarStatus(); break;
                case 7: remover(); break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("\n-- Cadastro de Entrega --");

        System.out.print("Código da entrega: ");
        String codigo = scanner.nextLine();
        if (codigo.trim().isEmpty()) {
            System.out.println("O código não pode ficar vazio.");
            return;
        }
        if (buscarPorCodigo(codigo) != null) {
            System.out.println("Já existe uma entrega com esse código.");
            return;
        }

        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Cliente cliente = clienteController.buscarPorCpf(cpf);
        if (cliente == null) {
            System.out.println("Cliente não encontrado. Cadastre-o primeiro.");
            return;
        }

        Endereco destino = cliente.getEndereco();
        System.out.println("Endereço do cliente: " + destino.formatar());
        System.out.print("Entregar em outro endereço? (s/n): ");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("s")) {
            destino = lerEndereco();
        }

        Entrega entrega = new Entrega(codigo, cliente, destino);
        lerItens(entrega);

        if (entrega.getItens().isEmpty()) {
            System.out.println("A entrega precisa ter ao menos um item. Cadastro cancelado.");
            return;
        }

        entregas.add(entrega);
        System.out.println("Entrega cadastrada com sucesso!");
    }

    private void listar() {
        System.out.println("\n-- Lista de Entregas --");
        if (entregas.isEmpty()) {
            System.out.println("Nenhuma entrega cadastrada.");
            return;
        }
        for (Entrega e : entregas) {
            System.out.println(e);
            System.out.println("------------------------");
        }
    }

    public Entrega buscarPorCodigo(String codigo) {
        for (Entrega e : entregas) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                return e;
            }
        }
        return null;
    }

    private void buscarInterativo() {
        System.out.print("Informe o código da entrega: ");
        Entrega entrega = buscarPorCodigo(scanner.nextLine());
        if (entrega != null) {
            System.out.println(entrega);
        } else {
            System.out.println("Entrega não encontrada.");
        }
    }

    private void adicionarItem() {
        Entrega entrega = pedirEntrega();
        if (entrega != null) {
            lerItens(entrega);
        }
    }

    private void removerItem() {
        Entrega entrega = pedirEntrega();
        if (entrega == null) {
            return;
        }

        System.out.print("Código do produto a remover: ");
        String codigoProduto = scanner.nextLine();
        boolean removido = entrega.removerItem(codigoProduto);
        if (removido) {
            System.out.println("Item removido com sucesso!");
        } else {
            System.out.println("Item não encontrado nesta entrega.");
        }
    }

    private void atualizarStatus() {
        Entrega entrega = pedirEntrega();
        if (entrega == null) {
            return;
        }

        System.out.println("Status atual: " + entrega.getStatus());
        System.out.println("1 - Iniciar trânsito");
        System.out.println("2 - Confirmar entrega");
        System.out.println("3 - Cancelar entrega");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = lerInteiro();

        if (opcao == 1) {
            entrega.iniciarTransito();
        } else if (opcao == 2) {
            entrega.confirmarEntrega();
        } else if (opcao == 3) {
            entrega.cancelar();
        } else if (opcao == 0) {
            return;
        } else {
            System.out.println("Opção inválida.");
            return;
        }
        System.out.println("Status atualizado para " + entrega.getStatus() + ".");
    }

    private void remover() {
        Entrega entrega = pedirEntrega();
        if (entrega != null) {
            entregas.remove(entrega);
            System.out.println("Entrega removida com sucesso!");
        }
    }

    private Entrega pedirEntrega() {
        System.out.print("Código da entrega: ");
        Entrega entrega = buscarPorCodigo(scanner.nextLine());
        if (entrega == null) {
            System.out.println("Entrega não encontrada.");
        }
        return entrega;
    }

    private void lerItens(Entrega entrega) {
        while (true) {
            System.out.print("Código do produto (vazio para finalizar): ");
            String codigo = scanner.nextLine();
            if (codigo.trim().isEmpty()) {
                return;
            }

            Produto produto = produtoController.buscarPorCodigo(codigo);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            System.out.print("Quantidade: ");
            int quantidade = lerInteiro();
            if (quantidade <= 0) {
                System.out.println("A quantidade precisa ser maior que zero.");
                continue;
            }

            entrega.adicionarItem(produto, quantidade);
            System.out.println("Item adicionado: " + produto.getNome() + " x" + quantidade);
        }
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

    private int lerInteiro() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Entrada inválida. Digite um número: ");
        }
        int numero = scanner.nextInt();
        scanner.nextLine();
        return numero;
    }
}