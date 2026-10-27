<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>
<c:set var="vNome" value="${form.nome[0]}"/>
<c:set var="vDocumento" value="${form.documento[0]}"/>
<c:set var="vTelefone" value="${form.telefone[0]}"/>
<c:set var="vRua" value="${form.rua[0]}"/>
<c:set var="vNumero" value="${form.numero[0]}"/>
<c:set var="vBairro" value="${form.bairro[0]}"/>
<c:set var="vCep" value="${form.cep[0]}"/>
<c:set var="vCidade" value="${form.cidade[0]}"/>
<c:set var="vEstado" value="${form.estado[0]}"/>

<h2>Novo cliente</h2>

<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="salvar">
    <input type="hidden" name="edicao" value="false">

    <label>Nome
        <input type="text" name="nome" placeholder="Nome completo ou razão social" required maxlength="150" value="<c:out value='${vNome}'/>" autofocus>
    </label>
    <div class="linha">
        <label>CPF/CNPJ
            <input type="text" name="documento" placeholder="000.000.000-00 ou 00.000.000/0000-00" required maxlength="14" inputmode="numeric"
                   pattern="\d{11}|\d{14}" title="CPF com 11 dígitos ou CNPJ com 14 dígitos (somente números)"
                   oninput="this.value=this.value.replace(/\D/g,'')"
                   value="<c:out value='${vDocumento}'/>">
        </label>
        <label>Telefone
            <input type="text" name="telefone" placeholder="(00) 00000-0000" required maxlength="11" inputmode="numeric" pattern="\d{10,11}" title="DDD + número, 10 ou 11 dígitos (somente números)" oninput="this.value=this.value.replace(/\D/g,'')" value="<c:out value='${vTelefone}'/>">
        </label>
    </div>

    <h3>Endereço</h3>
    <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
    <p class="dica">Outros endereços podem ser adicionados depois, na página do cliente.</p>

    <div class="botoes">
        <button type="submit" class="botao">Salvar</button>
        <a href="${ctx}/clientes">Cancelar</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/clientes/mascaras.jspf" %>
<%@ include file="/WEB-INF/views/rodape.jspf" %>
