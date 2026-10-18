package service;

public class ServiceException extends RuntimeException {
    public ServiceException(String mensagem, Throwable causa) {
        super(causa != null && causa.getMessage() != null ? mensagem + " Detalhe: " + causa.getMessage() : mensagem, causa);
    }
}
