$(document).ready(function() {
    $.ajax({
        url: `/market/id`,
        type: 'GET',
        success: function(marketId) {
            console.log("search-list data  : ", marketId);
            if(marketId) {
                orderTabAdd(marketId);
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching order data:', error);
        }
    });
})

function orderTabAdd(marketId) {
    // img 태그 추가
    const imgTag = `<img src="/images/order-icon.svg" alt="store_image" class="margin-top icon-size"/>`;
    $('#order-text-box').before(imgTag);

    // '주문' div 추가
    const orderDiv = `
                <div class="main-menu margin-bottom-30 linked-menu" onclick="location.href='/market/${marketId}/order'">
                    주문
                </div>
            `;
    $('#order-text-box').append(orderDiv);
    $('#order-container').append(orderDiv);
}



