package dao;

public class DAOFactory {
    private DAOFactory() {
    }

    public static ProdutoDAO criarProdutoDAO() {
        return new ProdutoDAOJdbc();
    }
}
