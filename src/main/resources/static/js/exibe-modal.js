const modalEvento = document.getElementById('rec-detalhes');
modalEvento.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget; // Botão que acionou o modal
    const id = button.getAttribute('data-id');
    const equipamento = button.getAttribute('data-equipamento');
    const marca = button.getAttribute('data-marca');
    const modelo = button.getAttribute('data-modelo');
    const serie = button.getAttribute('data-serie');
    const dtRec = button.getAttribute('data-dtRec');
    const cliente = button.getAttribute('data-cliente');
    const email = button.getAttribute('data-email');
    const tel = button.getAttribute('data-tel');
    const cidade = button.getAttribute('data-cidade');
    const estado = button.getAttribute('data-uf');
    const observ = button.getAttribute('data-obs');

    document.getElementById('equipamentoModal').innerText = equipamento;
    document.getElementById('marcaModal').innerText = marca;
    document.getElementById('modeloModal').innerText = modelo;
    document.getElementById('serieModal').innerText = serie;
    document.getElementById('dtRecModal').innerText = dtRec;
    document.getElementById('clienteModal').innerText = cliente;
    document.getElementById('emailModal').innerText = email;
    document.getElementById('telModal').innerText = tel;
    document.getElementById('cidadeModal').innerText = cidade;
    document.getElementById('ufModal').innerText = estado;
    document.getElementById('obsModal').innerText = observ;
});