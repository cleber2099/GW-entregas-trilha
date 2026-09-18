import models.Produto;

public class App {
    public static void main(String[] args) throws Exception {


        Produto produto1 = new Produto("Produto 1", "Descrição do Produto 1", 10.0, 5, "P001");
        
                System.out.println(produto1.getNome());


    }
}
