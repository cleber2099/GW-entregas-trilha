package dao;

public class DAOFactory {
    private DAOFactory() {
    }

    public static ProdutoDAO criarProdutoDAO() {
        return new ProdutoDAOJdbc();
    }

    public static ClienteDAO criarClienteDAO() {
        return new ClienteDAOJdbc();
    }

    public static EntregaDAO criarEntregaDAO() {
        return new EntregaDAOJdbc();
    }
}
