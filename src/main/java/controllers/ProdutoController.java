package controllers;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Produto;
import service.ProdutoService;
import service.ServiceException;

@WebServlet("/produtos")
public class ProdutoController extends ControllerBase {
    private final ProdutoService service = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if ("novo".equals(acao)) {
            mostrar(req, resp, "produtos/form.jsp");
        } else if ("editar".equals(acao)) {
            editar(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            if (busca != null && !busca.trim().isEmpty()) {
                Produto produto = service.buscarPorCodigo(busca.trim());
                req.setAttribute("produtos", produto != null
                        ? Collections.singletonList(produto) : Collections.emptyList());
            } else {
                req.setAttribute("produtos", service.listar());
            }
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
        }
        req.setAttribute("busca", busca);
        mostrar(req, resp, "produtos/lista.jsp");
    }

    private void editar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Produto produto = service.buscarPorCodigo(req.getParameter("codigo"));
            if (produto == null) {
                redirecionar(req, resp, "/produtos", "Produto não encontrado.", true);
                return;
            }
            req.setAttribute("produto", produto);
            req.setAttribute("edicao", true);
            mostrar(req, resp, "produtos/form.jsp");
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, "/produtos", e.getMessage(), true);
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
            int estoque = lerInteiro(req.getParameter("estoque"), "Estoque");
            if (edicao) {
                service.editar(codigo, nome, peso, volume, valor, estoque);
                redirecionar(req, resp, "/produtos", "Produto atualizado com sucesso!", false);
            } else {
                service.cadastrar(codigo, nome, peso, volume, valor, estoque);
                redirecionar(req, resp, "/produtos", "Produto cadastrado com sucesso!", false);
            }
        } catch (IllegalArgumentException | ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("edicao", edicao);
            req.setAttribute("form", req.getParameterMap());
            mostrar(req, resp, "produtos/form.jsp");
        }
    }

    private void remover(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            service.remover(req.getParameter("codigo"));
            redirecionar(req, resp, "/produtos", "Produto removido com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, "/produtos", e.getMessage(), true);
        }
    }
}
