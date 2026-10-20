package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.entrega.Entrega;
import service.ClienteService;
import service.EntregaService;
import service.ProdutoService;
import service.ServiceException;

@WebServlet("/entregas")
public class EntregaController extends ControllerBase {
    private final EntregaService service = new EntregaService();
    private final ClienteService clienteService = new ClienteService();
    private final ProdutoService produtoService = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if ("novo".equals(acao)) {
            novo(req, resp);
        } else if ("detalhe".equals(acao)) {
            detalhe(req, resp);
        } else {
            listar(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        String codigo = req.getParameter("codigo");
        if ("salvar".equals(acao)) {
            salvar(req, resp);
            return;
        }
        if ("remover".equals(acao)) {
            remover(req, resp, codigo);
            return;
        }

        try {
            if ("adicionarItem".equals(acao)) {
                service.adicionarItem(codigo, req.getParameter("produto"),
                        lerInteiro(req.getParameter("quantidade"), "Quantidade"));
                voltarAoDetalhe(req, resp, codigo, "Produto adicionado.", false);
            } else if ("removerItem".equals(acao)) {
                service.removerItem(codigo, req.getParameter("produto"));
                voltarAoDetalhe(req, resp, codigo, "Produto removido.", false);
            } else if ("iniciarTransporte".equals(acao)) {
                service.iniciarTransporte(codigo);
                voltarAoDetalhe(req, resp, codigo, "Entrega saiu para transporte.", false);
            } else if ("confirmarEntrega".equals(acao)) {
                service.confirmarEntrega(codigo);
                voltarAoDetalhe(req, resp, codigo, "Entrega confirmada.", false);
            } else if ("cancelar".equals(acao)) {
                service.cancelar(codigo);
                voltarAoDetalhe(req, resp, codigo, "Entrega cancelada.", false);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida.");
            }
        } catch (IllegalArgumentException | IllegalStateException | ServiceException e) {
            voltarAoDetalhe(req, resp, codigo, e.getMessage(), true);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("entregas", service.listar());
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
        }
        mostrar(req, resp, "entregas/lista.jsp");
    }

    private void novo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("clientes", clienteService.listar());
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
        }
        mostrar(req, resp, "entregas/form.jsp");
    }

    private void detalhe(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Entrega entrega = service.buscarPorCodigo(req.getParameter("codigo"));
            if (entrega == null) {
                redirecionar(req, resp, "/entregas", "Entrega não encontrada.", true);
                return;
            }
            req.setAttribute("entrega", entrega);
            if (entrega.podeAlterarItens()) {
                req.setAttribute("produtos", produtoService.listar());
            }
            mostrar(req, resp, "entregas/detalhe.jsp");
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, "/entregas", e.getMessage(), true);
        }
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codigo = req.getParameter("codigo");
        try {
            int origemId = lerInteiro(req.getParameter("origem"), "Endereço de origem");
            int destinoId = lerInteiro(req.getParameter("destino"), "Endereço de destino");
            Entrega entrega = service.cadastrar(codigo, origemId, destinoId);
            voltarAoDetalhe(req, resp, entrega.getCodigo(),
                    "Entrega cadastrada! Agora adicione os produtos.", false);
        } catch (IllegalArgumentException | ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("form", req.getParameterMap());
            novo(req, resp);
        }
    }

    private void remover(HttpServletRequest req, HttpServletResponse resp, String codigo) throws IOException {
        try {
            service.remover(codigo);
            redirecionar(req, resp, "/entregas", "Entrega removida com sucesso!", false);
        } catch (IllegalArgumentException | IllegalStateException | ServiceException e) {
            voltarAoDetalhe(req, resp, codigo, e.getMessage(), true);
        }
    }

    private void voltarAoDetalhe(HttpServletRequest req, HttpServletResponse resp, String codigo,
                                 String mensagem, boolean erro) throws IOException {
        redirecionar(req, resp, "/entregas?acao=detalhe&codigo=" + url(codigo), mensagem, erro);
    }
}
