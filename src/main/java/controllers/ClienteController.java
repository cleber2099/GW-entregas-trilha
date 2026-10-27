package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Cliente;
import models.Endereco;
import service.ClienteService;
import service.ServiceException;

@WebServlet("/clientes")
public class ClienteController extends ControllerBase {
    private final ClienteService service = new ClienteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        if ("novo".equals(acao)) {
            mostrar(req, resp, "clientes/form.jsp");
        } else if ("detalhe".equals(acao)) {
            mostrarCliente(req, resp, "clientes/detalhe.jsp");
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
        } else if ("adicionarEndereco".equals(acao)) {
            adicionarEndereco(req, resp);
        } else if ("removerEndereco".equals(acao)) {
            removerEndereco(req, resp);
        } else if ("salvarEndereco".equals(acao)) {
            salvarEndereco(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ação inválida.");
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("busca", req.getParameter("busca"));
            req.setAttribute("clientes", service.buscar(req.getParameter("busca")));
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
        }
        mostrar(req, resp, "clientes/lista.jsp");
    }

    private void mostrarCliente(HttpServletRequest req, HttpServletResponse resp, String pagina)
            throws ServletException, IOException {
        try {
            Cliente cliente = service.buscarPorDocumento(req.getParameter("documento"));
            if (cliente == null) {
                redirecionar(req, resp, "/clientes", "Cliente não encontrado.", true);
                return;
            }
            req.setAttribute("cliente", cliente);
            mostrar(req, resp, pagina);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, "/clientes", e.getMessage(), true);
        }
    }

    private void salvar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean edicao = "true".equals(req.getParameter("edicao"));
        String nome = req.getParameter("nome");
        String telefone = req.getParameter("telefone");
        String documento = req.getParameter("documento");
        try {
            if (edicao) {
                Cliente cliente = service.editar(documento, nome, telefone);
                redirecionar(req, resp, detalhe(cliente), "Cliente atualizado com sucesso!", false);
            } else {
                Cliente cliente = service.cadastrar(nome, telefone, documento, lerEndereco(req));
                redirecionar(req, resp, detalhe(cliente), "Cliente cadastrado com sucesso!", false);
            }
        } catch (IllegalArgumentException | ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("form", req.getParameterMap());
            if (edicao) {
                req.setAttribute("formDe", "dados");
                mostrarCliente(req, resp, "clientes/detalhe.jsp");
            } else {
                mostrar(req, resp, "clientes/form.jsp");
            }
        }
    }

    private void remover(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            service.remover(req.getParameter("documento"));
            redirecionar(req, resp, "/clientes", "Cliente removido com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, "/clientes", e.getMessage(), true);
        }
    }

    private void adicionarEndereco(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String documento = req.getParameter("documento");
        try {
            service.adicionarEndereco(documento, lerEndereco(req));
            redirecionar(req, resp, "/clientes?acao=detalhe&documento=" + url(documento),
                    "Endereço adicionado com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("form", req.getParameterMap());
            req.setAttribute("formDe", "novo");
            mostrarCliente(req, resp, "clientes/detalhe.jsp");
        }
    }

    private void salvarEndereco(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String documento = req.getParameter("documento");
        try {
            int enderecoId = lerInteiro(req.getParameter("enderecoId"), "Endereço");
            service.editarEndereco(documento, enderecoId, lerEndereco(req));
            redirecionar(req, resp, "/clientes?acao=detalhe&documento=" + url(documento),
                    "Endereço atualizado com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("form", req.getParameterMap());
            req.setAttribute("formDe", req.getParameter("enderecoId"));
            mostrarCliente(req, resp, "clientes/detalhe.jsp");
        }
    }

    private void removerEndereco(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String documento = req.getParameter("documento");
        String voltar = "/clientes?acao=detalhe&documento=" + url(documento);
        try {
            service.removerEndereco(documento, lerInteiro(req.getParameter("enderecoId"), "Endereço"));
            redirecionar(req, resp, voltar, "Endereço removido com sucesso!", false);
        } catch (IllegalArgumentException | ServiceException e) {
            redirecionar(req, resp, voltar, e.getMessage(), true);
        }
    }

    private Endereco lerEndereco(HttpServletRequest req) {
        return new Endereco(
                req.getParameter("rua"),
                req.getParameter("cidade"),
                req.getParameter("estado"),
                req.getParameter("cep"),
                req.getParameter("numero"),
                req.getParameter("bairro"));
    }

    private String detalhe(Cliente cliente) {
        return "/clientes?acao=detalhe&documento=" + cliente.getDocumento();
    }
}
