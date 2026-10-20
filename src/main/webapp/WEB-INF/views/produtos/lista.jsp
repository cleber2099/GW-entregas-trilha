<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<div class="topo">
    <h2>Produtos</h2>
    <a class="botao" href="${ctx}/produtos?acao=novo">Novo produto</a>
</div>

<form class="busca" method="get" action="${ctx}/produtos">
    <input type="text" name="busca" placeholder="Buscar por código" value="<c:out value='${busca}'/>">
    <button type="submit">Buscar</button>
    <c:if test="${not empty busca}"><a href="${ctx}/produtos">Limpar</a></c:if>
</form>

<c:choose>
    <c:when test="${empty produtos}">
        <p class="vazio">${not empty busca ? 'Produto não encontrado.' : 'Nenhum produto cadastrado.'}</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Código</th>
                <th>Nome</th>
                <th class="num">Peso (kg)</th>
                <th class="num">Volume (m³)</th>
                <th class="num">Valor</th>
                <th class="num">Estoque</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${produtos}">
                <tr>
                    <td><c:out value="${p.codigo}"/></td>
                    <td><c:out value="${p.nome}"/></td>
                    <td class="num"><fmt:formatNumber value="${p.peso}" minFractionDigits="2" maxFractionDigits="3"/></td>
                    <td class="num"><fmt:formatNumber value="${p.volume}" minFractionDigits="3" maxFractionDigits="3"/></td>
                    <td class="num"><fmt:formatNumber value="${p.valor}" type="currency"/></td>
                    <td class="num">${p.estoque}</td>
                    <td class="acoes">
                        <c:url var="urlEditar" value="/produtos">
                            <c:param name="acao" value="editar"/>
                            <c:param name="codigo" value="${p.codigo}"/>
                        </c:url>
                        <a href="${urlEditar}">Editar</a>
                        <form method="post" action="${ctx}/produtos" onsubmit="return confirm('Excluir este produto?');">
                            <input type="hidden" name="acao" value="remover">
                            <input type="hidden" name="codigo" value="<c:out value='${p.codigo}'/>">
                            <button type="submit" class="link perigo">Excluir</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
