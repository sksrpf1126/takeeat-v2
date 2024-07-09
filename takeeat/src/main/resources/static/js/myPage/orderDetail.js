document.addEventListener('DOMContentLoaded', function() {
    //=== 옵션 가격 계산 후 출력 ===
    var optionAndPriceElements = document.querySelectorAll('.optionAndPrice');
    optionAndPriceElements.forEach(function(element) {
        var totalOptionPrice = 0;

        var optionPriceElements = element.querySelectorAll('.optionPrice');
        optionPriceElements.forEach(function(priceElement) {
            var price = parseInt(priceElement.getAttribute('data-option-price'));
            totalOptionPrice += price;
        });

        if (totalOptionPrice > 0) {
            var totalOptionPriceElement = element.querySelector('.totalOptionPrice');
            totalOptionPriceElement.textContent = '+' + totalOptionPrice.toLocaleString() + '원';
        }
    });

    //=== 총 주문금액 계산 후 출력 ===
    var menuContainerElements = document.querySelectorAll('.menuContainer');
    var totalPrice = 0;
    menuContainerElements.forEach(function(element) {
        var totalMenuPriceElement = element.querySelector('.totalMenuPrice');

        var price = totalMenuPriceElement.getAttribute('data-menu-price');
        var quantity = totalMenuPriceElement.getAttribute('data-menu-quantity');

        totalPrice += (price * quantity);
    });

    var totalPriceElement = document.getElementById('totalPrice');
    totalPriceElement.textContent = totalPrice.toLocaleString() + '원';

    var orderBtnContentElement = document.getElementById('orderBtnContent');
    orderBtnContentElement.textContent = totalPrice.toLocaleString() + '원 결제하기';

});