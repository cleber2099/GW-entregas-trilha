package util;

public final class Validador {

    private Validador() {
    }

    public static void exigirTexto(String valor, String mensagem) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public static void exigirNaoNulo(Object valor, String mensagem) {
        if (valor == null) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public static void exigirNaoNegativo(double valor, String mensagem) {
        if (valor < 0) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public static void exigirPositivo(int valor, String mensagem) {
        if (valor <= 0) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
