<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<h2>Nova entrega</h2>

<c:choose>
    <c:when test="${empty clientes}">
        <p class="vazio">Cadastre ao menos um cliente antes de criar uma entrega.
            <a href="${ctx}/clientes?acao=novo">Novo cliente</a></p>
    </c:when>
    <c:otherwise>
        <form class="cadastro" method="post" action="${ctx}/entregas">
            <input type="hidden" name="acao" value="salvar">

            <label>Código da entrega
                <input type="text" name="codigo" required maxlength="30" value="<c:out value='${form.codigo[0]}'/>" autofocus>
            </label>

            <label>Origem (remetente)
                <select name="origem" required>
                    <option value="">Escolha o endereço de origem</option>
                    <c:forEach var="cli" items="${clientes}">
                        <optgroup label="<c:out value='${cli.nome}'/>">
                            <c:forEach var="end" items="${cli.enderecos}">
                                <option value="${end.id}" ${form.origem[0] == end.id ? 'selected' : ''}><c:out value="${end.formatar()}"/></option>
                            </c:forEach>
                        </optgroup>
                    </c:forEach>
                </select>
            </label>

            <label>Destino (destinatário)
                <select name="destino" required>
                    <option value="">Escolha o endereço de destino</option>
                    <c:forEach var="cli" items="${clientes}">
                        <optgroup label="<c:out value='${cli.nome}'/>">
                            <c:forEach var="end" items="${cli.enderecos}">
                                <option value="${end.id}" ${form.destino[0] == end.id ? 'selected' : ''}><c:out value="${end.formatar()}"/></option>
                            </c:forEach>
                        </optgroup>
                    </c:forEach>
                </select>
            </label>
            <p class="dica">O destino não pode ser o mesmo endereço da origem.
                Os produtos são adicionados na próxima tela.</p>

            <div class="botoes">
                <button type="submit" class="botao">Criar entrega</button>
                <a href="${ctx}/entregas">Cancelar</a>
            </div>
        </form>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
