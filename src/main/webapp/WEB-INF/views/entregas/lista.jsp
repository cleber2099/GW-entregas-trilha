<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<div class="topo">
    <h2>Entregas</h2>
    <a class="botao" href="${ctx}/entregas?acao=novo">Nova entrega</a>
</div>

<c:choose>
    <c:when test="${empty entregas}">
        <p class="vazio">Nenhuma entrega cadastrada.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Código</th>
                <th>Remetente</th>
                <th>Destinatário</th>
                <th>Status</th>
                <th class="num">Valor</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="e" items="${entregas}">
                <c:url var="urlDetalhe" value="/entregas">
                    <c:param name="acao" value="detalhe"/>
                    <c:param name="codigo" value="${e.codigo}"/>
                </c:url>
                <tr>
                    <td><a href="${urlDetalhe}"><c:out value="${e.codigo}"/></a></td>
                    <td><c:out value="${e.remetente.nome}"/></td>
                    <td><c:out value="${e.destinatario.nome}"/></td>
                    <td><span class="status status-${e.status}">${e.status.descricao}</span></td>
                    <td class="num"><fmt:formatNumber value="${e.calcularValorTotal()}" type="currency"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
