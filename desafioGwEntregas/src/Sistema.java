import java.util.Scanner;
import controllers.ProdutoController;

 
public class Sistema {
 
    private Scanner scanner = new Scanner(System.in);
    private ProdutoController produtoController = new ProdutoController(scanner);
 
    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n Sistema GW Entregas ");
            System.out.println("1 - Gerenciar Produtos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());
 
            switch (opcao) {
                case 1: produtoController.menuProduto(); break;
                case 0: System.out.println("Encerrando o sistema..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
 
        scanner.close();
    }
}