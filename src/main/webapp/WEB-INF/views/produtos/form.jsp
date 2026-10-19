<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Valores: "form" (o que foi digitado, após erro) tem prioridade sobre "produto" (edição). --%>
<c:set var="vCodigo" value="${not empty form ? form.codigo[0] : produto.codigo}"/>
<c:set var="vNome" value="${not empty form ? form.nome[0] : produto.nome}"/>
<c:set var="vPeso" value="${not empty form ? form.peso[0] : produto.peso}"/>
<c:set var="vVolume" value="${not empty form ? form.volume[0] : produto.volume}"/>
<c:set var="vValor" value="${not empty form ? form.valor[0] : produto.valor}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${edicao ? 'Editar' : 'Novo'} produto - GW Entregas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<header>
    <h1>GW Entregas</h1>
</header>
<main>
    <h2>${edicao ? 'Editar produto' : 'Novo produto'}</h2>

    <c:if test="${not empty erro}"><p class="alerta erro"><c:out value="${erro}"/></p></c:if>

    <form class="cadastro" method="post" action="${pageContext.request.contextPath}/produtos">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="edicao" value="${edicao ? 'true' : 'false'}">

        <label>Código
            <input type="text" name="codigo" required maxlength="30"
                   value="<c:out value='${vCodigo}'/>" ${edicao ? 'readonly' : 'autofocus'}>
        </label>
        <label>Nome
            <input type="text" name="nome" required maxlength="150"
                   value="<c:out value='${vNome}'/>" ${edicao ? 'autofocus' : ''}>
        </label>
        <div class="linha">
            <label>Peso (kg)
                <input type="text" name="peso" required inputmode="decimal" value="<c:out value='${vPeso}'/>">
            </label>
            <label>Volume (m³)
                <input type="text" name="volume" required inputmode="decimal" value="<c:out value='${vVolume}'/>">
            </label>
            <label>Valor (R$)
                <input type="text" name="valor" required inputmode="decimal" value="<c:out value='${vValor}'/>">
            </label>
        </div>

        <div class="botoes">
            <button type="submit" class="botao">Salvar</button>
            <a href="${pageContext.request.contextPath}/produtos">Cancelar</a>
        </div>
    </form>
</main>
</body>
</html>
