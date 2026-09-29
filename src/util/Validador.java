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

    public static void exigirMinimo(double valor, double minimo, String mensagem) {
        if (valor < minimo) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
