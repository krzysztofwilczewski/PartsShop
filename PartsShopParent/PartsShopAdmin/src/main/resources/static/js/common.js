function showModalDialog(title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorModal(message) {
    showModalDialog("Błąd", message);
}

function showWarningModal(message) {
    showModalDialog("UWAGA", message);
}