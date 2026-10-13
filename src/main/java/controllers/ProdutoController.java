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
            System.out.print("Código: ");
            String codigo = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            try {
                if (buscarPorCodigo(codigo) != null) {
                    System.out.println("Já existe um produto com esse código.");
                    return;
                }
                double peso = lerDecimal("Peso (kg): ");
                double volume = lerDecimal("Volume (m³): ");
                double valor = lerDecimal("Valor (R$): ");
                produtos.add(new Produto(codigo, nome, peso, volume, valor));
                System.out.println("Produto cadastrado com sucesso!");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        private void listarProdutos() {
            System.out.println("Lista de Produtos:");
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }
            for (Produto produto : produtos) {
                System.out.println(produto);
            }
        }

        private double lerDecimal(String rotulo) {
            System.out.print(rotulo);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Número inválido.");
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
        String nome = scanner.nextLine();

        try {
            double peso = lerDecimal("Novo peso (" + produto.getPeso() + " kg): ");
            double volume = lerDecimal("Novo volume (" + produto.getVolume() + " m³): ");
            double valor = lerDecimal("Novo valor (" + produto.getValor() + "): ");
            produto.setNome(nome);
            produto.setPeso(peso);
            produto.setVolume(volume);
            produto.setValor(valor);
            System.out.println("Produto atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
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
