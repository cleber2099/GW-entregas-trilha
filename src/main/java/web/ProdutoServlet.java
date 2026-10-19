package web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Produto;
import service.ProdutoService;
import service.ServiceException;

/**
 * Gerenciamento de produtos pela web.
 *
 * GET  /produtos                      lista (com busca opcional por código)
 * GET  /produtos?acao=novo            formulário de cadastro
 * GET  /produtos?acao=editar&codigo=X formulário de edição
 * POST /produtos acao=salvar          cadastra ou atualiza
 * POST /produtos acao=remover         exclui
 */
@WebServlet("/produtos")
public class ProdutoServlet extends HttpServlet {
    private static final String LISTA = "/WEB-INF/views/produtos/lista.jsp";
    private static final String FORM = "/WEB-INF/views/produtos/form.jsp";

    private final ProdutoService service = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if ("novo".equals(acao)) {
            req.getRequestDispatcher(FORM).forward(req, resp);
        } else if ("editar".equals(acao)) {
            editar(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String acao = req.getParameter("acao");
        if ("salvar".equals(acao)) {
            salvar(req, resp);
        } else if ("remover".equals(acao)) {
            remover(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida.");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String busca = req.getParameter("busca");
        try {
            List<Produto> produtos;
            if (busca != null && !busca.trim().isEmpty()) {
                Produto produto = service.buscarPorCodigo(busca.trim());
                produtos = produto != null ? Collections.singletonList(produto) : Collections.<Produto>emptyList();
            } else {
                produtos = service.listar();
            }
            req.setAttribute("produtos", produtos);
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("produtos", Collections.emptyList());
        }
        req.setAttribute("busca", busca);
        moverMensagemDaSessao(req);
        req.getRequestDispatcher(LISTA).forward(req, resp);
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Produto produto = service.buscarPorCodigo(req.getParameter("codigo"));
            if (produto == null) {
                redirecionar(req, resp, "Produto não encontrado.", true);
                return;
            }
            req.setAttribute("produto", produto);
            req.setAttribute("edicao", true);
            req.getRequestDispatcher(FORM).forward(req, resp);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, e.getMessage(), true);
        }
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean edicao = "true".equals(req.getParameter("edicao"));
        String codigo = req.getParameter("codigo");
        String nome = req.getParameter("nome");
        try {
            double peso = lerDecimal(req.getParameter("peso"), "Peso");
            double volume = lerDecimal(req.getParameter("volume"), "Volume");
            double valor = lerDecimal(req.getParameter("valor"), "Valor");
            if (edicao) {
                service.editar(codigo, nome, peso, volume, valor);
                redirecionar(req, resp, "Produto atualizado com sucesso!", false);
            } else {
                service.cadastrar(codigo, nome, peso, volume, valor);
                redirecionar(req, resp, "Produto cadastrado com sucesso!", false);
            }
        } catch (IllegalArgumentException | ServiceException e) {
            // volta ao formulário mantendo o que o usuário digitou
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("edicao", edicao);
            req.setAttribute("form", req.getParameterMap());
            req.getRequestDispatcher(FORM).forward(req, resp);
        }
    }

    private void remover(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            service.remover(req.getParameter("codigo"));
            redirecionar(req, resp, "Produto removido com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, e.getMessage(), true);
        }
    }

    private double lerDecimal(String valor, String campo) {
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException(campo + ": número inválido.");
        }
    }

    /** Post/Redirect/Get: a mensagem sobrevive ao redirect pela sessão. */
    private void redirecionar(HttpServletRequest req, HttpServletResponse resp, String mensagem, boolean erro)
            throws IOException {
        req.getSession().setAttribute(erro ? "erro" : "sucesso", mensagem);
        resp.sendRedirect(req.getContextPath() + "/produtos");
    }

    private void moverMensagemDaSessao(HttpServletRequest req) {
        if (req.getSession(false) == null) {
            return;
        }
        for (String chave : new String[] {"sucesso", "erro"}) {
            Object mensagem = req.getSession().getAttribute(chave);
            if (mensagem != null) {
                req.setAttribute(chave, mensagem);
                req.getSession().removeAttribute(chave);
            }
        }
    }
}
