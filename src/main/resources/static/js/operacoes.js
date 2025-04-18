$("#form-buscar-equip").submit(function(event) {
    event.preventDefault();

    var equipamento = $("#rec-equipamento").val();

    //console.log(">>> Equipamento: ", equipamento);

    $.ajax({
        method: "GET",
        url: "/equipamento/buscar-equipamento",
        data: {numSerie: equipamento},
        success: function(dados) {
            console.log(dados);
            $("#descricao").val(dados.descricao);
            $("#marca").val(dados.marca);
            $("#modelo").val(dados.modelo);
            $("#num-serie").val(dados.numSerie);
            $("#equipamento-id").val(dados.id);
        },
        error: function(xhr) {
            $("#alert-rec").addClass("alert alert-danger").text("Esse número de série não está cadastrado");
        }
    });
});

$("#select-cliente").blur(function() {
    var clienteid = $(this).val();
    //console.log("ID cliente:", clienteid);

    $.ajax({
        method: "GET",
        url: "/cliente/buscar-cliente",
        data: {id: clienteid},
        success: function(cli) {
            console.log("\n>>> Sucesso --------------------", cli);
            $("#contato").val(cli.telefone);
            $("#email").val(cli.email);
            $("#cliente-id").val(cli.id);
        },
        error: function(xhr) {
            console.log("\n>>> Erro --------------", xhr.responseText);
        }
    });
});

$("#btn-detalhes").on("click", function() {
    var recid = $(this).val();
    console.log(">>> Recebimento: ", recid);
});