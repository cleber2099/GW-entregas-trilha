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

    public static void exigirPositivo(double valor, String mensagem) {
        if (!(valor > 0)) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    public static String exigirDocumento(String valor, String mensagem) {
        exigirTexto(valor, mensagem);
        String digitos = valor.replaceAll("\\D", "");
        if (digitos.length() != 11 && digitos.length() != 14) {
            throw new IllegalArgumentException("O documento deve ser um CPF (11 dígitos) ou CNPJ (14 dígitos).");
        }
        return digitos;
    }
}
