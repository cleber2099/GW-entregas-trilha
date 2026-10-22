<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<h2>Editar endereço</h2>
<p><c:out value="${cliente.nome}"/></p>

<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="salvarEndereco">
    <input type="hidden" name="documento" value="${cliente.documento}">
    <input type="hidden" name="enderecoId" value="<c:out value='${enderecoId}'/>">
    <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
    <div class="botoes">
        <button type="submit" class="botao">Salvar</button>
        <a href="${ctx}/clientes?acao=detalhe&amp;documento=${cliente.documento}">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
