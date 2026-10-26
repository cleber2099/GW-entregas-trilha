<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>
<c:set var="vCodigo" value="${not empty form ? form.codigo[0] : produto.codigo}"/>
<c:set var="vNome" value="${not empty form ? form.nome[0] : produto.nome}"/>
<c:set var="vPeso" value="${not empty form ? form.peso[0] : produto.peso}"/>
<c:set var="vVolume" value="${not empty form ? form.volume[0] : produto.volume}"/>
<c:set var="vValor" value="${not empty form ? form.valor[0] : produto.valor}"/>
<c:set var="vEstoque" value="${not empty form ? form.estoque[0] : (empty produto ? 0 : produto.estoque)}"/>

<h2>${edicao ? 'Editar produto' : 'Novo produto'}</h2>

<form class="cadastro" method="post" action="${ctx}/produtos">
    <input type="hidden" name="acao" value="salvar">
    <input type="hidden" name="edicao" value="${edicao ? 'true' : 'false'}">

    <label>Código
        <input type="text" name="codigo" placeholder="Ex.: PROD-001" required maxlength="30"
               value="<c:out value='${vCodigo}'/>" ${edicao ? 'readonly' : 'autofocus'}>
    </label>
    <label>Nome
        <input type="text" name="nome" placeholder="Nome do produto" required maxlength="150" value="<c:out value='${vNome}'/>">
    </label>
    <div class="linha">
        <label>Peso (kg)
            <input type="text" name="peso" placeholder="Ex.: 1,5" required inputmode="decimal" oninput="this.value=this.value.replace(/[^\d.,]/g,'')" value="<c:out value='${vPeso}'/>">
        </label>
        <label>Volume (m³)
            <input type="text" name="volume" placeholder="Ex.: 0,02" required inputmode="decimal" oninput="this.value=this.value.replace(/[^\d.,]/g,'')" value="<c:out value='${vVolume}'/>">
        </label>
        <label>Valor (R$)
            <input type="text" name="valor" placeholder="Ex.: 29,90" required inputmode="decimal" oninput="this.value=this.value.replace(/[^\d.,]/g,'')" value="<c:out value='${vValor}'/>">
        </label>
        <label>Estoque (unidades)
            <input type="number" name="estoque" placeholder="Ex.: 100" required min="0" step="1" value="<c:out value='${vEstoque}'/>">
        </label>
    </div>
    <p class="dica">O valor fica gravado em cada entrega no momento em que o produto é adicionado;
        mudar o preço aqui não altera entregas já feitas.</p>

    <div class="botoes">
        <button type="submit" class="botao">Salvar</button>
        <a href="${ctx}/produtos">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
