<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<div class="topo">
    <h2><c:out value="${cliente.nome}"/></h2>
    <a href="${ctx}/clientes">&larr; Voltar para clientes</a>
</div>

<section class="cartao">
    <p><strong>CPF/CNPJ:</strong> ${cliente.documento}</p>
    <p><strong>Telefone:</strong> <c:out value="${cliente.telefone}"/></p>
    <div class="botoes">
        <a class="botao" href="${ctx}/clientes?acao=editar&amp;documento=${cliente.documento}">Editar dados</a>
        <form method="post" action="${ctx}/clientes" onsubmit="return confirm('Excluir este cliente e os endereços dele?');">
            <input type="hidden" name="acao" value="remover">
            <input type="hidden" name="documento" value="${cliente.documento}">
            <button type="submit" class="link perigo">Excluir cliente</button>
        </form>
    </div>
</section>

<h3>Endereços</h3>
<table>
    <tbody>
    <c:forEach var="end" items="${cliente.enderecos}">
        <tr>
            <td><c:out value="${end.formatar()}"/></td>
            <td class="acoes">
                <a href="${ctx}/clientes?acao=editarEndereco&amp;documento=${cliente.documento}&amp;enderecoId=${end.id}">Editar</a>
                <form method="post" action="${ctx}/clientes" onsubmit="return confirm('Remover este endereço?');">
                    <input type="hidden" name="acao" value="removerEndereco">
                    <input type="hidden" name="documento" value="${cliente.documento}">
                    <input type="hidden" name="enderecoId" value="${end.id}">
                    <button type="submit" class="link perigo">Remover</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<h3>Adicionar endereço</h3>
<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="adicionarEndereco">
    <input type="hidden" name="documento" value="${cliente.documento}">
    <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
    <div class="botoes">
        <button type="submit" class="botao">Adicionar</button>
    </div>
</form>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
