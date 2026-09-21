package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Produto;

public class ProdutoController {
    private List<Produto> produtos = new ArrayList<>();
    Scanner scanner;

   public ProdutoController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void  menuProduto() {
        int opcao;
        do {
            System.out.println(" Menu Para Gerir Produtos");
            System.out.println("1. Cadastrar Produto");
            System.out.println("2. Listar Produtos");
            System.out.println("3. Buscar por Código");
            System.out.println("4. Editar Produto");
            System.out.println("5. Excluir Produto");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); 
            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    buscarInterativo();
                    break;
                case 4:
                    editar();
                break;
                case 5:
                    remover();
                break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

    }
        private void cadastrarProduto() {
            System.out.println("Cadastro de Produto");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            System.out.print("Preço: ");
            double preco = scanner.nextDouble();
            scanner.nextLine(); 
            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            Produto produto = new Produto(nome, descricao, preco, codigo);
            produtos.add(produto);
            System.out.println("Produto cadastrado com sucesso!");
        }

        private void listarProdutos() {
            System.out.println("Lista de Produtos:");
            for (Produto produto : produtos) {
                System.out.println("Código: " + produto.getCodigo());
                System.out.println("Nome: " + produto.getNome());
                System.out.println("Descrição: " + produto.getDescricao());
                System.out.println("Preço: " + produto.getPreco());
                System.out.println("------------------------");
            }
        }

        public Produto buscarPorCodigo(String codigo) {
            for (Produto p : produtos) {
                if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
                }
            }
            return null;
        }
private void buscarInterativo() {
        System.out.print("Informe o código: ");
        Produto produto = buscarPorCodigo(scanner.nextLine());
        System.out.println(produto != null ? produto : "Produto não encontrado.");
    }
 
    private void editar() {
        System.out.print("Código do produto a editar: ");
        Produto produto = buscarPorCodigo(scanner.nextLine());
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.print("Novo nome (" + produto.getNome() + "): ");
        produto.setNome(scanner.nextLine());
        System.out.print("Nova descrição (" + produto.getDescricao() + "): ");
        produto.setDescricao(scanner.nextLine());
        System.out.print("Novo preço (" + produto.getPreco() + "): ");
        produto.setPreco(Double.parseDouble(scanner.nextLine().replace(",", ".")));
        System.out.println("Produto atualizado com sucesso!");
    }
 
    private void remover() {
        System.out.print("Código do produto a remover: ");
        Produto produto = buscarPorCodigo(scanner.nextLine());
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        produtos.remove(produto);
        System.out.println("Produto removido com sucesso!");
    }
   

}
