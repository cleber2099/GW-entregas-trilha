<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>
<c:set var="vNome" value="${not empty form ? form.nome[0] : cliente.nome}"/>
<c:set var="vDocumento" value="${not empty form ? form.documento[0] : cliente.documento}"/>
<c:set var="vTelefone" value="${not empty form ? form.telefone[0] : cliente.telefone}"/>

<h2>${edicao ? 'Editar cliente' : 'Novo cliente'}</h2>

<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="salvar">
    <input type="hidden" name="edicao" value="${edicao ? 'true' : 'false'}">

    <label>Nome
        <input type="text" name="nome" required maxlength="150" value="<c:out value='${vNome}'/>" autofocus>
    </label>
    <div class="linha">
        <label>CPF/CNPJ
            <input type="text" name="documento" required maxlength="14" inputmode="numeric"
                   pattern="\d{11}|\d{14}" title="CPF com 11 dígitos ou CNPJ com 14 dígitos (somente números)"
                   oninput="this.value=this.value.replace(/\D/g,'')"
                   value="<c:out value='${vDocumento}'/>" ${edicao ? 'readonly' : ''}>
        </label>
        <label>Telefone
            <input type="text" name="telefone" required maxlength="11" inputmode="numeric" pattern="\d{10,11}" title="DDD + número, 10 ou 11 dígitos (somente números)" oninput="this.value=this.value.replace(/\D/g,'')" value="<c:out value='${vTelefone}'/>">
        </label>
    </div>

    <c:if test="${not edicao}">
        <h3>Endereço</h3>
        <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
        <p class="dica">Outros endereços podem ser adicionados depois, na página do cliente.</p>
    </c:if>

    <div class="botoes">
        <button type="submit" class="botao">Salvar</button>
        <a href="${ctx}/clientes">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
