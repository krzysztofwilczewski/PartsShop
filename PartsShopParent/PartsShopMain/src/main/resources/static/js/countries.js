var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoad.click(function () {
        loadCountries();
    });
    dropDownCountry.on("change", function () {
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.click(function () {

        if (buttonAddCountry.val() == "Dodaj"){
            addCountry();
        } else {
            changeFormStateToNewCountry();
        }
    });
    buttonUpdateCountry.click(function () {
        updateCountry();
    });
    buttonDeleteCountry.click(function () {
        deleteCountry();
    });
});

function deleteCountry(){
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0];

    url = contextPath + "admin/countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNewCountry();
        showToastMessage("Kraj został usunięty");
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}

function updateCountry() {

    if (!validateFormCountry()) return;

    url = contextPath + "admin/countries/save";

    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    countryId =dropDownCountry.val().split("-")[0];

    jsonData = {id: countryId, name : countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessage("Kraj został zaktualizowany");

        changeFormStateToNewCountry();
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA")
    });
}

function validateFormCountry(){
    formCountry = document.getElementById("formCountry");
    if (!formCountry.checkValidity()) {
        formCountry.reportValidity();
        return false;
    }
    return true;
}

function addCountry(){

    if (!validateFormCountry()) return;

    url = contextPath + "admin/countries/save";

    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();

    jsonData = {name : countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {
        selectNewlyAddedCountry(countryId, countryCode, countryName);
        showToastMessage("Nowy kraj został dodany");
    }).fail(function () {
        showToastMessage("BŁĄD SERWERA")
    });

}

function selectNewlyAddedCountry(countryId, countryCode, countryName){
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}


function changeFormStateToNewCountry() {
    buttonAddCountry.val("Dodaj");
    labelCountryName.text("Nazwa kraju:");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop("value", "Nowy");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Wybrany kraj:");
    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);
}

function loadCountries() {
    url = contextPath + "admin/countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountry.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function () {
        buttonLoad.val("Odświeź listę");
        showToastMessage("Kraje zostały załadowane");

    }).fail(function () {
        showToastMessage("BŁĄD SERWERA");
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}