import java.util.Scanner;
import controllers.ProdutoController;
import controllers.ClienteController;
import controllers.EntregaController;


public class Sistema {

    private Scanner scanner = new Scanner(System.in);
    private ProdutoController produtoController = new ProdutoController(scanner);
    private  ClienteController clienteController = new ClienteController(scanner);
    private EntregaController entregaController = new EntregaController(scanner, clienteController, produtoController);

    public void iniciar() {
        int opcao;
        do {
            System.out.println("\n Sistema GW Entregas ");
            System.out.println("1 - Gerenciar Produtos");
            System.out.println("2 - Gerenciar Clientes");
            System.out.println("3 - Gerenciar Entregas");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1: produtoController.menuProduto(); break;
                case 2: clienteController.menu(); break;
                case 3: entregaController.menu(); break;
                case 0: System.out.println("Encerrando o sistema..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
