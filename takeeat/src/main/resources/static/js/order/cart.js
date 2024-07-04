//==== 삭제 confirm 모달 ====
function showDeleteModal() {
    const modal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
    modal.show();
}

$(document).ready(function() {

    window.stepUpAndUpdate = function(button) {
        var input = button.parentNode.querySelector('input[type=number]');
        input.stepUp();
        $(input).trigger('change');
    }

    window.stepDownAndUpdate = function(button) {
        var input = button.parentNode.querySelector('input[type=number]');
        input.stepDown();
        $(input).trigger('change');
    }

    //=== totalPrice 계산 ===
    function updateTotalPrice() {
        var totalPrice = 0;

        $('.menuPrice').each(function() {
            var menuPrice = parseInt($(this).data('menu-total-price'));
            totalPrice += menuPrice;
        });

        $('#totalPrice strong').text(totalPrice.toLocaleString() + '원');
    }

    updateTotalPrice();

    $('.quantity').on('change', function() {
        const cartMenuId = $(this).data('menu-id');
        const quantity = parseInt($(this).val());

        var menuPriceElement = $(this).closest('.numberPriceContainer').find('.menuPrice');
        const cartMenuPrice = menuPriceElement.data('menu-price');
        const newMenuPrice = cartMenuPrice * quantity;
        menuPriceElement.data('menu-total-price', newMenuPrice);
        menuPriceElement.find('strong').text(newMenuPrice.toLocaleString() + '원');

        $.ajax({
            url: '/updateQuantity',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                cartMenuId: cartMenuId,
                quantity: quantity
            }),
            success: function(response) {
                updateTotalPrice();
            },
            error: function(xhr, status, error) {
                alert('메뉴 수량 변경 중 오류가 발생했습니다.')
            }
        });
    });
});