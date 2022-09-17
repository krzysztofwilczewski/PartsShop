var dropDownCountry;
var dataListState;
var fieldState;

$(document).ready(function () {
    dropDownCountry = $("#country");
    dataListState = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function () {
        loadAllStatesForCountry();
        fieldState.val("").focus();
    })
});

function loadAllStatesForCountry(){
    selectedCountry = $("#country option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "register/list_states_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dataListState.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        });
    });
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value != $("#password").val()) {
        confirmPassword.setCustomValidity("Hasła nie są zgodne!");
    } else {
        confirmPassword.setCustomValidity("");
    }
}

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