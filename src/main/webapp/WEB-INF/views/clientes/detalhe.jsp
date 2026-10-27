<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>
<c:set var="vNome" value="${formDe == 'dados' ? form.nome[0] : cliente.nome}"/>
<c:set var="vTelefone" value="${formDe == 'dados' ? form.telefone[0] : cliente.telefone}"/>

<div class="topo">
    <h2><c:out value="${cliente.nome}"/></h2>
    <a href="${ctx}/clientes">&larr; Voltar para clientes</a>
</div>

<h3>Dados</h3>
<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="salvar">
    <input type="hidden" name="edicao" value="true">
    <input type="hidden" name="documento" value="${cliente.documento}">

    <label>Nome
        <input type="text" name="nome" placeholder="Nome completo ou razão social" required maxlength="150" value="<c:out value='${vNome}'/>">
    </label>
    <div class="linha">
        <label>CPF/CNPJ
            <input type="text" name="documento" value="<c:out value='${cliente.documento}'/>" readonly disabled>
        </label>
        <label>Telefone
            <input type="text" name="telefone" placeholder="(00) 00000-0000" required maxlength="11" inputmode="numeric" pattern="\d{10,11}" title="DDD + número, 10 ou 11 dígitos (somente números)" value="<c:out value='${vTelefone}'/>">
        </label>
    </div>
    <div class="botoes">
        <button type="submit" class="botao">Salvar dados</button>
    </div>
</form>
<form method="post" action="${ctx}/clientes" onsubmit="return confirm('Excluir este cliente e os endereços dele?');">
    <input type="hidden" name="acao" value="remover">
    <input type="hidden" name="documento" value="${cliente.documento}">
    <button type="submit" class="link perigo">Excluir cliente</button>
</form>

<h3>Endereços</h3>
<c:if test="${empty cliente.enderecos}"><p class="vazio">Nenhum endereço cadastrado.</p></c:if>
<c:forEach var="end" items="${cliente.enderecos}">
    <c:set var="doForm" value="${formDe == end.id.toString()}"/>
    <c:set var="vRua" value="${doForm ? form.rua[0] : end.rua}"/>
    <c:set var="vNumero" value="${doForm ? form.numero[0] : end.numero}"/>
    <c:set var="vBairro" value="${doForm ? form.bairro[0] : end.bairro}"/>
    <c:set var="vCep" value="${doForm ? form.cep[0] : end.cep}"/>
    <c:set var="vCidade" value="${doForm ? form.cidade[0] : end.cidade}"/>
    <c:set var="vEstado" value="${doForm ? form.estado[0] : end.estado}"/>
    <form class="cadastro" method="post" action="${ctx}/clientes">
        <input type="hidden" name="acao" value="salvarEndereco">
        <input type="hidden" name="documento" value="${cliente.documento}">
        <input type="hidden" name="enderecoId" value="${end.id}">
        <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
        <div class="botoes">
            <button type="submit" class="botao">Salvar endereço</button>
            <button type="submit" class="link perigo" form="remover-${end.id}">Remover</button>
        </div>
    </form>
    <form id="remover-${end.id}" method="post" action="${ctx}/clientes" onsubmit="return confirm('Remover este endereço?');">
        <input type="hidden" name="acao" value="removerEndereco">
        <input type="hidden" name="documento" value="${cliente.documento}">
        <input type="hidden" name="enderecoId" value="${end.id}">
    </form>
</c:forEach>

<h3>Adicionar endereço</h3>
<c:set var="doForm" value="${formDe == 'novo'}"/>
<c:set var="vRua" value="${doForm ? form.rua[0] : ''}"/>
<c:set var="vNumero" value="${doForm ? form.numero[0] : ''}"/>
<c:set var="vBairro" value="${doForm ? form.bairro[0] : ''}"/>
<c:set var="vCep" value="${doForm ? form.cep[0] : ''}"/>
<c:set var="vCidade" value="${doForm ? form.cidade[0] : ''}"/>
<c:set var="vEstado" value="${doForm ? form.estado[0] : ''}"/>
<form class="cadastro" method="post" action="${ctx}/clientes">
    <input type="hidden" name="acao" value="adicionarEndereco">
    <input type="hidden" name="documento" value="${cliente.documento}">
    <%@ include file="/WEB-INF/views/clientes/enderecoCampos.jspf" %>
    <div class="botoes">
        <button type="submit" class="botao">Adicionar</button>
    </div>
</form>

<%@ include file="/WEB-INF/views/clientes/mascaras.jspf" %>
<%@ include file="/WEB-INF/views/rodape.jspf" %>
