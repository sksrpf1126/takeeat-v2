$(document).ready(function() {
    connect();
})

function connect() {

    var socket = new SockJS('/connect/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/notification-market/' +  marketId, function (data) {
            const responseData = JSON.parse(data.body);

            console.log("responseData Object : ", responseData);

            const orderList = $('.order-list');
            const newOrderItem = createOrderItem(responseData);
            orderList.prepend(newOrderItem);

            $('.order-item').off('click').click(function () {
                const orderId = $(this).data('order-id');
                $(this).find('.new-badge').remove();
                $('.order-item').removeClass('active-background');
                $(this).addClass('active-background');
                fetchOrderDetails(orderId);
            });

            fetchOrderStatusCount(marketId);
        });

        stompClient.subscribe('/topic/reply/' + marketId, function (message) {
            // 서버 응답을 받은 후에 후속 작업 실행
            fetchOrderStatusCount(marketId);
            fetchOrderList(marketId, status, sortOrder);
        });
    });
}

function fetchOrderStatusCount(marketId) {
    $.ajax({
        url: '/market/' + marketId + '/order-status/count',
        type: 'GET',
        success: function(data) {
            $('#order-status-wait').text(data.WAIT + "건");
            $('#order-status-accept').text(data.ACCEPT + "건");
            $('#order-status-complete').text(data.COMPLETE + "건");
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('알림을 가져오는 중 오류 발생:', textStatus, errorThrown);
        }
    });
}

function handleRejectClick(orderId, memberId, orderStatus) {
    alert("해당 주문을 거절하였습니다.");

    if(!!stompClient) {
        const sendMessageData = {
            marketId : marketId,
            orderId : orderId,
            currentOrderStatus : orderStatus,
            selectOrderStatus : "REJECT"
        };

        stompClient.send("/app/send-member/" + memberId, {}, JSON.stringify(sendMessageData));

    }else {
        alert("서버와의 연결이 끊겼습니다. 다시 접속해 주세요.");
    }
}

function handleAcceptClick(orderId, memberId, orderStatus) {
    alert("해당 주문을 수락하였습니다.");

    if(!!stompClient) {
        const sendMessageData = {
            marketId : marketId,
            orderId : orderId,
            currentOrderStatus : orderStatus,
            selectOrderStatus : "ACCEPT"
        };

        stompClient.send("/app/send-member/" + memberId, {}, JSON.stringify(sendMessageData));

    }else {
        alert("서버와의 연결이 끊겼습니다. 다시 접속해 주세요.");
    }
}

function handleCancelClick(orderId, memberId, orderStatus) {
    alert("해당 주문을 취소하였습니다.");

    if(!!stompClient) {
        const sendMessageData = {
            marketId : marketId,
            orderId : orderId,
            currentOrderStatus : orderStatus,
            selectOrderStatus : "CANCEL"
        };

        stompClient.send("/app/send-member/" + memberId, {}, JSON.stringify(sendMessageData));

    }else {
        alert("서버와의 연결이 끊겼습니다. 다시 접속해 주세요.");
    }
}

function handleCompleteClick(orderId, memberId, orderStatus) {
    alert("해당 주문을 완료 처리하였습니다.");

    if(!!stompClient) {
        const sendMessageData = {
            marketId : marketId,
            orderId : orderId,
            currentOrderStatus : orderStatus,
            selectOrderStatus : "COMPLETE"
        };

        stompClient.send("/app/send-member/" + memberId, {}, JSON.stringify(sendMessageData));

    }else {
        alert("서버와의 연결이 끊겼습니다. 다시 접속해 주세요.");
    }
}

