package models;

public class Entrega {

    public enum Status {
        PENDENTE,
        EM_TRANSITO,
        ENTREGUE,
        CANCELADO
    }


    private String codigo;
    private Cliente cliente;
    private Endereco enderecoDestino;
    private Status status;

}
