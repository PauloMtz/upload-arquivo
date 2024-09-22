$("#form-buscar").submit(function(event) {
    event.preventDefault();

    var codigoProduto = $("#codigoProduto").val();

    //console.log(">>> Código: ", codigoProduto);

    $.ajax({
        method: "GET",
        url: "/estoque/buscar-produto",
        data: {busca: codigoProduto},
        beforeSend: function() {
            $("#form-entrada").each(function() {
                this.reset(); // limpa os campos do formulário
            })
            $("#quant").removeClass("invalid-feedback").text("");
            $("#preco-erro").removeClass("invalid-feedback").text("");
            $("#quantidade").removeClass("is-invalid").addClass("form-control");
            $("#preco-campo").removeClass("is-invalid").addClass("form-control");
            $("#fotoProduto").attr("src", "");
            $("#alert").removeClass("alert alert-danger").text("");
        },
        success: function(dados) {
            //console.log(dados);
            $("#nome").val(dados.nome);
            $("#codigo").val(dados.codigo);
            $("#idProduto").val(dados.id);
            $("#fotoProduto").attr("src", "/estoque/mostrarImagem/" + dados.foto);
            //var produtoID = $("#idProduto").val();
            //console.log("\n>>> ID produto: ", produtoID);
            document.querySelector('#idProduto').innerHTML = `${dados.id}`;
        },
        error: function(xhr) {
            //console.log(">>> erro: ", xhr.responseText);
            $("#fotoProduto").attr("src", "/img/camera.png");
            $("#alert").addClass("alert alert-danger").text("O código pesquisado não está cadastrado");
        },
    });

})