$(document).ready(function() {
    connect();
})

function connect() {

    var socket = new SockJS('/connect/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("연결에 성공하였습니다!!!", $("#user-userId").val());
        stompClient.subscribe('/topic/notification-member/' +  $("#user-userId").val(), function (data) {
            console.log("response data : ", data);
        });
    });
}