package controllers;

import java.util.List;
import java.util.Scanner;
import models.Cliente;
import models.Endereco;
import models.entrega.Entrega;
import models.Produto;
import service.EntregaService;
import service.ServiceException;

public class EntregaController {

    private EntregaService service = new EntregaService();
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
            System.out.println("4 - Adicionar produto");
            System.out.println("5 - Remover produto");
            System.out.println("6 - Atualizar status (realizada / não realizada)");
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

        Cliente remetente = pedirCliente("Remetente");
        if (remetente == null) {
            return;
        }
        Cliente destinatario = pedirCliente("Destinatário");
        if (destinatario == null) {
            return;
        }

        Entrega entrega;
        try {
            Endereco origem = escolherEndereco("origem", remetente);
            Endereco destino = escolherEndereco("destino", destinatario);
            entrega = new Entrega(codigo, remetente, destinatario, origem, destino);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
            return;
        }

        lerItens(entrega);

        if (entrega.estaVazia()) {
            System.out.println("A entrega precisa ter ao menos um item. Cadastro cancelado.");
            return;
        }

        try {
            service.cadastrar(entrega);
            System.out.println("Entrega cadastrada com sucesso!");
        } catch (IllegalArgumentException | ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listar() {
        System.out.println("\n-- Lista de Entregas --");
        List<Entrega> entregas;
        try {
            entregas = service.listar();
        } catch (ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
            return;
        }
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
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        try {
            return service.buscarPorCodigo(codigo);
        } catch (ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
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
        if (entrega == null) {
            return;
        }
        if (entrega.getStatus() != Entrega.Status.PENDENTE) {
            System.out.println("Erro: A entrega não pode ser alterada no status " + entrega.getStatus() + ".");
            return;
        }
        lerItens(entrega);
        salvarItens(entrega);
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
            if (salvarItens(entrega)) {
                System.out.println("Produto removido com sucesso!");
            }
        } else if (entrega.getStatus() == Entrega.Status.PENDENTE) {
            System.out.println("Produto não encontrado nesta entrega.");
        }
    }

    private void atualizarStatus() {
        Entrega entrega = pedirEntrega();
        if (entrega == null) {
            return;
        }

        System.out.println("Status atual: " + entrega.getStatus());
        System.out.println("1 - Marcar como realizada");
        System.out.println("2 - Marcar como não realizada");
        System.out.println("0 - Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = lerInteiro();

        Entrega.Status anterior = entrega.getStatus();
        if (opcao == 1) {
            entrega.marcarRealizada();
        } else if (opcao == 2) {
            entrega.marcarNaoRealizada();
        } else if (opcao == 0) {
            return;
        } else {
            System.out.println("Opção inválida.");
            return;
        }
        if (entrega.getStatus() == anterior) {
            return;
        }
        try {
            service.atualizarStatus(entrega);
            System.out.println("Status atualizado para " + entrega.getStatus() + ".");
        } catch (ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void remover() {
        Entrega entrega = pedirEntrega();
        if (entrega == null) {
            return;
        }
        try {
            service.remover(entrega.getCodigo());
            System.out.println("Entrega removida com sucesso!");
        } catch (IllegalArgumentException | ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private boolean salvarItens(Entrega entrega) {
        try {
            service.atualizarItens(entrega);
            return true;
        } catch (ServiceException e) {
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

    private Cliente pedirCliente(String papel) {
        System.out.print("CPF/CNPJ do " + papel.toLowerCase() + ": ");
        Cliente cliente = clienteController.buscarPorDocumento(scanner.nextLine());
        if (cliente == null) {
            System.out.println(papel + " não encontrado. Cadastre-o primeiro.");
        }
        return cliente;
    }

    private Endereco escolherEndereco(String tipo, Cliente cliente) {
        Endereco endereco = cliente.getEndereco();
        System.out.println("Endereço de " + tipo + " (cadastro de " + cliente.getNome() + "): " + endereco.formatar());
        System.out.print("Usar outro endereço de " + tipo + "? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            return lerEndereco();
        }
        return endereco;
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
            System.out.println("Produto adicionado: " + produto.getNome() + " x" + quantidade);
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
