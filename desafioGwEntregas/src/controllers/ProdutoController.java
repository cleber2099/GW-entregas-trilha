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
            System.out.print("Quantidade: ");
            int quantidade = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Código: ");
            String codigo = scanner.nextLine();

            Produto produto = new Produto(nome, descricao, preco, quantidade, codigo);
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
                System.out.println("Quantidade: " + produto.getQuantidade());
                System.out.println("------------------------");
            }
        }

   

}
