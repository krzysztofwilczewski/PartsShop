$(document).ready(function () {
$(".linkMinus").on("click", function (event) {
    event.preventDefault();
    productId = $(this).attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
    } else {
        showWarningModal('Minimalna ilość to 1');
    }
});
    $(".linkPlus").on("click", function (event) {
        event.preventDefault();
        productId = $(this).attr("pid");
        quantityInput = $("#quantity" + productId);
        newQuantity = parseInt(quantityInput.val()) + 1;

        if (newQuantity <= 5) {
            quantityInput.val(newQuantity);
        } else {
            showWarningModal('Maksymalna ilość to 5');
        }
    });
});