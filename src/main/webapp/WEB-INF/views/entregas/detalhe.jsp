<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/cabecalho.jspf" %>

<div class="topo">
    <h2>Entrega <c:out value="${entrega.codigo}"/>
        <span class="status status-${entrega.status}">${entrega.status.descricao}</span></h2>
    <a href="${ctx}/entregas">&larr; Voltar para entregas</a>
</div>

<section class="cartao duas-colunas">
    <div>
        <h3>Origem</h3>
        <p><strong><c:out value="${entrega.remetente.nome}"/></strong> (${entrega.remetente.documento})</p>
        <p><c:out value="${entrega.enderecoOrigem.formatar()}"/></p>
    </div>
    <div>
        <h3>Destino</h3>
        <p><strong><c:out value="${entrega.destinatario.nome}"/></strong> (${entrega.destinatario.documento})</p>
        <p><c:out value="${entrega.enderecoDestino.formatar()}"/></p>
    </div>
</section>

<h3>Produtos</h3>
<c:choose>
    <c:when test="${entrega.estaVazia()}">
        <p class="vazio">Nenhum produto nesta entrega.</p>
    </c:when>
    <c:otherwise>
        <table>
            <thead>
            <tr>
                <th>Produto</th>
                <th class="num">Qtd.</th>
                <th class="num">Peso (kg)</th>
                <th class="num">Volume (m³)</th>
                <th class="num">Valor</th>
                <c:if test="${entrega.podeAlterarItens()}"><th></th></c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${entrega.itens}">
                <tr>
                    <td><c:out value="${item.key.nome}"/> (<c:out value="${item.key.codigo}"/>)</td>
                    <td class="num">${item.value}</td>
                    <td class="num"><fmt:formatNumber value="${item.key.peso * item.value}" minFractionDigits="2" maxFractionDigits="3"/></td>
                    <td class="num"><fmt:formatNumber value="${item.key.volume * item.value}" minFractionDigits="3" maxFractionDigits="3"/></td>
                    <td class="num"><fmt:formatNumber value="${item.key.valor * item.value}" type="currency"/></td>
                    <c:if test="${entrega.podeAlterarItens()}">
                        <td class="acoes">
                            <form method="post" action="${ctx}/entregas">
                                <input type="hidden" name="acao" value="removerItem">
                                <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
                                <input type="hidden" name="produto" value="<c:out value='${item.key.codigo}'/>">
                                <button type="submit" class="link perigo">Remover</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
            <tfoot>
            <tr>
                <th>Total</th>
                <th></th>
                <th class="num"><fmt:formatNumber value="${entrega.calcularPesoTotal()}" minFractionDigits="2" maxFractionDigits="3"/></th>
                <th class="num"><fmt:formatNumber value="${entrega.calcularVolumeTotal()}" minFractionDigits="3" maxFractionDigits="3"/></th>
                <th class="num"><fmt:formatNumber value="${entrega.calcularValorTotal()}" type="currency"/></th>
                <c:if test="${entrega.podeAlterarItens()}"><th></th></c:if>
            </tr>
            </tfoot>
        </table>
    </c:otherwise>
</c:choose>

<c:if test="${entrega.podeAlterarItens()}">
    <form class="busca" method="post" action="${ctx}/entregas">
        <input type="hidden" name="acao" value="adicionarItem">
        <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
        <select name="produto" required>
            <option value="">Escolha um produto</option>
            <c:forEach var="p" items="${produtos}">
                <option value="<c:out value='${p.codigo}'/>" ${p.estoque == 0 ? 'disabled' : ''}>
                    <c:out value="${p.nome}"/> - <fmt:formatNumber value="${p.valor}" type="currency"/>
                    (estoque: ${p.estoque})</option>
            </c:forEach>
        </select>
        <input type="number" name="quantidade" min="1" value="1" required class="quantidade">
        <button type="submit">Adicionar produto</button>
    </form>
</c:if>

<h3>Status</h3>
<p class="dica">Pendente &rarr; Em trânsito &rarr; Entregue. Pendente ou em trânsito pode ser cancelada
    (cancelar ou excluir devolve os produtos ao estoque).</p>
<div class="botoes">
    <c:if test="${entrega.podeIniciarTransporte()}">
        <form method="post" action="${ctx}/entregas">
            <input type="hidden" name="acao" value="iniciarTransporte">
            <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
            <button type="submit" class="botao">Iniciar transporte</button>
        </form>
    </c:if>
    <c:if test="${entrega.podeConfirmarEntrega()}">
        <form method="post" action="${ctx}/entregas">
            <input type="hidden" name="acao" value="confirmarEntrega">
            <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
            <button type="submit" class="botao">Confirmar entrega</button>
        </form>
    </c:if>
    <c:if test="${entrega.podeCancelar()}">
        <form method="post" action="${ctx}/entregas" onsubmit="return confirm('Cancelar esta entrega?');">
            <input type="hidden" name="acao" value="cancelar">
            <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
            <button type="submit" class="botao secundario">Cancelar entrega</button>
        </form>
    </c:if>
    <c:if test="${entrega.podeExcluir()}">
        <form method="post" action="${ctx}/entregas" onsubmit="return confirm('Excluir esta entrega?');">
            <input type="hidden" name="acao" value="remover">
            <input type="hidden" name="codigo" value="<c:out value='${entrega.codigo}'/>">
            <button type="submit" class="link perigo">Excluir</button>
        </form>
    </c:if>
    <c:if test="${not entrega.podeCancelar()}">
        <p class="vazio">Esta entrega está ${entrega.status.descricao.toLowerCase()} e não pode mais ser alterada.</p>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
