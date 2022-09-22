$(document).ready(function () {
    $("#buttonAddToCart").on("click", function () {
        addToCart();
    });
});

function addToCart() {
    quantity = $("#quantity" + productId).val();
    url = contextPath + "cart/add/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        showModalDialog("Koszyk", response);
    }).fail(function () {
        showErrorModal("Bład przy dodwaniau produktu do koszyka.");
    });
}