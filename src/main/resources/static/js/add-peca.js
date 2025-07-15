$(document).ready(function () {
    $("#btnBuscar").click(function (e) {
        e.preventDefault();

        let codigoProduto = $("#codigoProduto").val();

        if (codigoProduto) {
            $.ajax({
                url: "/estoque/buscar-produto",
                type: "GET",
                data: { busca: codigoProduto },
                success: function (response) {
                    // Preenche o input do produto com o nome retornado
                    $("#produtoInput").val(response.nome);
                },
                error: function (xhr) {
                    alert("Produto não encontrado " + xhr.responseText);
                }
            });
        } else {
            alert("Por favor, insira um código de produto.");
        }
    });

    let indexProduto = 0;

    $("#btnAdicionar").click(function (e) {
        e.preventDefault();

        let produtoNome = $("#produtoInput").val();
        let produtoCodigo = $("#codigoProduto").val();

        if (!produtoNome || !produtoCodigo) {
            alert("Nenhum produto foi buscado para adicionar.");
            return;
        }

        // Verifica duplicidade
        let jaExiste = $(`input[name='produtos[${indexProduto}].codigo'][value='${produtoCodigo}']`).length > 0;

        if ($("#listaProdutos input[type='hidden'][value='" + produtoCodigo + "']").length > 0) {
            alert("Este produto já foi adicionado.");
            return;
        }

        let novoProduto = `
            <div class="d-flex align-items-center mb-2">
                <input type="hidden" name="produtos[${indexProduto}].codigo" value="${produtoCodigo}" />
                <div class="form-check col-md-10">
                    <label class="form-check-label">${produtoNome}</label>
                </div>
                <div class="col-md-2">
                    <input type="number" name="produtos[${indexProduto}].quantidade" class="form-control form-control-sm" required />
                </div>
            </div>
        `;
        $("#listaProdutos").append(novoProduto);
        indexProduto++;
    });

    /*$("#btnAdicionar").click(function (e) {
        e.preventDefault();

        let produtoNome = $("#produtoInput").val();

        if (produtoNome) {
            // Cria novo bloco na lista de produtos
            let novoProduto = `
                    <div class="d-flex align-items-center">
                        <div class="form-check col-md-10">
                            <input class="form-check-input" id="produtoCheck" type="checkbox" checked>
                            <label class="form-check-label" id="produtoNome">${produtoNome}</label>
                        </div>
                        <div class="col-md-2">
                            <input type="number" name="qtdeProduto" class="form-control form-control-sm" id="qtdeProduto">
                        </div>
                    </div>
                `;
            $("#listaProdutos").append(novoProduto);
        } else {
            alert("Nenhum produto foi buscado para adicionar.");
        }
    });*/
});
