<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<div class="topo">
    <h2>Clientes</h2>
    <a class="botao" href="${ctx}/clientes?acao=novo">Novo cliente</a>
</div>

<c:choose>
    <c:when test="${empty clientes}">
        <p class="vazio">Nenhum cliente cadastrado.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Nome</th>
                <th>CPF/CNPJ</th>
                <th>Telefone</th>
                <th class="num">Endereços</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="cli" items="${clientes}">
                <tr>
                    <td><a href="${ctx}/clientes?acao=detalhe&amp;documento=${cli.documento}"><c:out value="${cli.nome}"/></a></td>
                    <td>${cli.documento}</td>
                    <td><c:out value="${cli.telefone}"/></td>
                    <td class="num">${cli.enderecos.size()}</td>
                    <td class="acoes">
                        <a href="${ctx}/clientes?acao=detalhe&amp;documento=${cli.documento}">Abrir</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
