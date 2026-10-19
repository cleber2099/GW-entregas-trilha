<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="pt_BR"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Produtos - GW Entregas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header>
    <h1>GW Entregas</h1>
</header>
<main>
    <div class="topo">
        <h2>Produtos</h2>
        <a class="botao" href="${pageContext.request.contextPath}/produtos?acao=novo">Novo produto</a>
    </div>

    <c:if test="${not empty sucesso}"><p class="alerta sucesso"><c:out value="${sucesso}"/></p></c:if>
    <c:if test="${not empty erro}"><p class="alerta erro"><c:out value="${erro}"/></p></c:if>

    <form class="busca" method="get" action="${pageContext.request.contextPath}/produtos">
        <input type="text" name="busca" placeholder="Buscar por código" value="<c:out value='${busca}'/>">
        <button type="submit">Buscar</button>
        <c:if test="${not empty busca}">
            <a href="${pageContext.request.contextPath}/produtos">Limpar</a>
        </c:if>
    </form>

    <c:choose>
        <c:when test="${empty produtos}">
            <p class="vazio">
                <c:choose>
                    <c:when test="${not empty busca}">Produto não encontrado.</c:when>
                    <c:otherwise>Nenhum produto cadastrado.</c:otherwise>
                </c:choose>
            </p>
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
                        <td class="acoes">
                            <c:url var="urlEditar" value="/produtos">
                                <c:param name="acao" value="editar"/>
                                <c:param name="codigo" value="${p.codigo}"/>
                            </c:url>
                            <a href="${urlEditar}">Editar</a>
                            <form method="post" action="${pageContext.request.contextPath}/produtos"
                                  onsubmit="return confirm('Excluir este produto?');">
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
</main>
</body>
</html>
