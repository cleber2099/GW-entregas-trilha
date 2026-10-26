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
        if (digitos.length() == 11 ? !cpfValido(digitos) : !cnpjValido(digitos)) {
            throw new IllegalArgumentException(digitos.length() == 11 ? "CPF inválido." : "CNPJ inválido.");
        }
        return digitos;
    }

    public static boolean cpfValido(String d) {
        if (d.matches("(\\d)\\1{10}")) {
            return false;
        }
        return digitoVerificador(d, 9, 10) == d.charAt(9) - '0'
                && digitoVerificador(d, 10, 11) == d.charAt(10) - '0';
    }

    public static boolean cnpjValido(String d) {
        if (d.matches("(\\d)\\1{13}")) {
            return false;
        }
        int[] p1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] p2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        return digitoVerificador(d, p1) == d.charAt(12) - '0' && digitoVerificador(d, p2) == d.charAt(13) - '0';
    }

    private static int digitoVerificador(String d, int tamanho, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (d.charAt(i) - '0') * (pesoInicial - i);
        }
        int resto = soma * 10 % 11;
        return resto == 10 ? 0 : resto;
    }

    private static int digitoVerificador(String d, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += (d.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    public static void exigirCep(String valor) {
        exigirTexto(valor, "O CEP é obrigatório.");
        String digitos = valor.replaceAll("\\D", "");
        if (digitos.length() != 8 || digitos.matches("0{8}")) {
            throw new IllegalArgumentException("CEP inválido.");
        }
    }
}
