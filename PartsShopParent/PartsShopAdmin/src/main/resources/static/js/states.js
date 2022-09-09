var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function () {
    buttonLoad4States = $("#buttonLoadCountriesForStates");
    dropDownCountry4States = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    buttonAddState = $("#buttonAddState");
    buttonUpdateState = $("#buttonUpdateState");
    buttonDeleteState = $("#buttonDeleteState");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");

    buttonLoad4States.click(function () {
        loadCountriesForStates();
    });

    dropDownCountry4States.on("change", function () {
        loadStatesForCountry();
    });

    dropDownStates.on("change", function () {
        changeFormStateToSelectedState();
    });

    buttonAddState.click(function () {
        if (buttonAddState.val() == "Dodaj") {
            addState();
        } else {
            changeFormStateToNew();
        }
    });

    buttonUpdateState.click(function () {
        updateState();
    });

    buttonDeleteState.click(function () {
        deleteState();
    });

});

function deleteState() {
    stateId = dropDownStates.val();

    url = contextPath + "admin/states/delete/" + stateId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownStates option[value='" + stateId + "']").remove();
        changeFormStateToNew();
        showToastMessage("Województwo zostało usunięte");
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}

function updateState() {
    if (!validateFormState()) return;

    url = contextPath + "admin/states/save";
    stateId = dropDownStates.val();
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId) {
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("Województwo/prowincja zsostało zaktualizowane");
        changeFormStateToNew();
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}

function addState() {
    if (!validateFormState()) return;

    url = contextPath + "admin/states/save";
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();

    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (stateId) {
        selectNewlyAddedState(stateId, stateName);
        showToastMessage("Nowe województwo/prowincja zostało dodane");
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });

}

function validateFormState() {
    formState = document.getElementById("formState");
    if (!formState.checkValidity()) {
        formState.reportValidity();
        return false;
    }

    return true;
}

function selectNewlyAddedState(stateId, stateName) {
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

    $("#dropDownStates option[value='" + stateId + "']").prop("selected", true);

    fieldStateName.val("").focus();
}

function changeFormStateToNew() {
    buttonAddState.val("Dodaj");
    labelStateName.text("Nazwa województwa:");

    buttonUpdateState.prop("disabled", true);
    buttonDeleteState.prop("disabled", true);

    fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
    buttonAddState.prop("value", "Nowy");
    buttonUpdateState.prop("disabled", false);
    buttonDeleteState.prop("disabled", false);

    labelStateName.text("Wybrane województwo:");

    selectedStateName = $("#dropDownStates option:selected").text();
    fieldStateName.val(selectedStateName);

}

function loadStatesForCountry() {
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "admin/states/list_by_country/" + countryId;

    $.get(url, function (responseJSON) {
        dropDownStates.empty();

        $.each(responseJSON, function (index, state) {
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });

    }).done(function () {
        changeFormStateToNew();
        showToastMessage("Załadowano wszystkie województwa/prowincje dla kraju " + selectedCountry.text());
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}

function loadCountriesForStates() {
    url = contextPath + "admin/countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountry4States.empty();

        $.each(responseJSON, function (index, country) {
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
        });

    }).done(function () {
        buttonLoad4States.val("Odśwież  listę");
        showToastMessage("Wszystkie kraje zostały załadowane");
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}