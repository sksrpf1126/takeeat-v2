$(document).ready(function() {
    connect();
})

function connect() {

    var socket = new SockJS('/connect/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("마켓 : 연결에 성공하였습니다!!!", marketId);
        stompClient.subscribe('/topic/notification-market/' +  marketId, function (data) {
            console.log("response market data : ", data);
        });
    });
}

function handleRejectClick(orderId, memberId, orderStatus) {
    alert("거부 버튼 클릭");
    console.log("orderId : ", orderId, "   orderStatus : ", orderStatus);

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