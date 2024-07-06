$('#payment-complete').on('click', function() {
    console.log("user payment-ok click!!", stompClient);
    paymentNotification();

});

function paymentNotification() {
    const marketId = $("#market-marketId").val();

    if(!!stompClient) {
        const testOrderRequest = {
            memberId: 1,
            requirement: "특별 요청사항을 여기에 입력하세요",
            totalPrice: 10000,  // 총 가격을 적절히 설정
            orderMenuRequests: [
                {
                    menuId: 1,
                    orderQuantity: 2,
                    optionIds: [1, 4, 5]  // 옵션 ID를 적절히 설정
                },
                {
                    menuId: 2,
                    orderQuantity: 1,
                    optionIds: [2, 5]  // 옵션 ID를 적절히 설정
                }
            ]
        };

        stompClient.send("/app/send-market/" + marketId, {}, JSON.stringify(testOrderRequest));
    }

}

function showNotification() {
    document.getElementById('bell-icon').classList.add('has-notification');
}

function hideNotification() {
    document.getElementById('bell-icon').classList.remove('has-notification');
}

document.getElementById('user-notification').addEventListener('click', function() {
    var panel = document.getElementById('notification-panel');
    if (panel.style.display === 'none' || panel.style.display === '') {
        panel.style.display = 'block';
    } else {
        panel.style.display = 'none';
    }
});

function fetchNotifications() {
    $.ajax({
        url: '/notification/scroll/' + $("#user-userId").val(),
        type: 'GET',
        success: function(data) {
            data.forEach(function(notification) {
                makeNotification(notification);
            });
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('알림을 가져오는 중 오류 발생:', textStatus, errorThrown);
        }
    });
}



$(document).ready(function() {
    fetchNotifications();
})