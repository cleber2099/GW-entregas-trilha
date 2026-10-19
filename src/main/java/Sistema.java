import java.util.Scanner;
import controllers.ClienteController;
import controllers.EntregaController;


public class Sistema {

    private Scanner scanner = new Scanner(System.in);
    private  ClienteController clienteController = new ClienteController(scanner);
    private EntregaController entregaController = new EntregaController(scanner, clienteController);

public void iniciar() {
    int opcao;
    do {
        System.out.println("\n Sistema GW Entregas ");
        System.out.println("(Produtos são gerenciados pela web: /entregasgw/produtos)");
        System.out.println("1 - Gerenciar Clientes");
        System.out.println("2 - Gerenciar Entregas");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");

        String entrada = scanner.nextLine().trim();

        try {
            opcao = Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            opcao = -1;
            continue;
        }

        switch (opcao) {
            case 1: clienteController.menu(); break;
            case 2: entregaController.menu(); break;
            case 0: System.out.println("Encerrando o sistema..."); break;
            default: System.out.println("Opção inválida.");
        }
    } while (opcao != 0);

    scanner.close();
}

}
