package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class ControllerBase extends HttpServlet {

    protected void mostrar(HttpServletRequest req, HttpServletResponse resp, String pagina)
            throws ServletException, IOException {
        trazerMensagemDaSessao(req);
        req.getRequestDispatcher("/WEB-INF/views/" + pagina).forward(req, resp);
    }

    protected void redirecionar(HttpServletRequest req, HttpServletResponse resp, String caminho,
                                String mensagem, boolean erro) throws IOException {
        req.getSession().setAttribute(erro ? "erro" : "sucesso", mensagem);
        resp.sendRedirect(req.getContextPath() + caminho);
    }

    protected double lerDecimal(String valor, String campo) {
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException(campo + ": número inválido.");
        }
    }

    protected int lerInteiro(String valor, String campo) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException(campo + ": informe um número inteiro.");
        }
    }

    protected String url(String valor) throws UnsupportedEncodingException {
        return valor == null ? "" : URLEncoder.encode(valor, "UTF-8");
    }

    private void trazerMensagemDaSessao(HttpServletRequest req) {
        HttpSession sessao = req.getSession(false);
        if (sessao == null) {
            return;
        }
        for (String chave : new String[] {"sucesso", "erro"}) {
            Object mensagem = sessao.getAttribute(chave);
            if (mensagem != null) {
                req.setAttribute(chave, mensagem);
                sessao.removeAttribute(chave);
            }
        }
    }
}
