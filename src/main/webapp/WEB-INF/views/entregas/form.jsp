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

            <label>Origem (remetente)
                <input type="text" class="filtro-endereco" autofocus placeholder="Buscar por nome, CPF/CNPJ ou endereço" autocomplete="off">
                <select name="origem" required>
                    <option value="">Escolha o endereço de origem</option>
                    <c:forEach var="cli" items="${clientes}">
                        <optgroup label="<c:out value='${cli.nome}'/>" data-busca="<c:out value='${cli.nome} ${cli.documento}'/>">
                            <c:forEach var="end" items="${cli.enderecos}">
                                <option value="${end.id}" ${form.origem[0] == end.id ? 'selected' : ''}><c:out value="${end.formatar()}"/></option>
                            </c:forEach>
                        </optgroup>
                    </c:forEach>
                </select>
            </label>

            <label>Destino (destinatário)
                <input type="text" class="filtro-endereco" placeholder="Buscar por nome, CPF/CNPJ ou endereço" autocomplete="off">
                <select name="destino" required>
                    <option value="">Escolha o endereço de destino</option>
                    <c:forEach var="cli" items="${clientes}">
                        <optgroup label="<c:out value='${cli.nome}'/>" data-busca="<c:out value='${cli.nome} ${cli.documento}'/>">
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

<script>
document.querySelectorAll('input.filtro-endereco').forEach(function (campo) {
    var select = campo.nextElementSibling;

    campo.addEventListener('input', function () {
        var termo = campo.value.trim().toLowerCase();

        // esconde o que não combina com a busca, sem mexer na estrutura do select
        select.querySelectorAll('optgroup').forEach(function (grupo) {
            var clienteCombina = grupo.dataset.busca.toLowerCase().includes(termo);
            var algumVisivel = false;

            grupo.querySelectorAll('option').forEach(function (opcao) {
                var combina = clienteCombina
                    || opcao.selected
                    || opcao.textContent.toLowerCase().includes(termo);
                opcao.hidden = !combina;
                if (combina) algumVisivel = true;
            });

            grupo.hidden = !algumVisivel;
        });
    });
});
</script>

<%@ include file="/WEB-INF/views/rodape.jspf" %>
