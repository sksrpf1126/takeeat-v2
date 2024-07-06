$(document).ready(function() {
    connect();
})

function connect() {

    var socket = new SockJS('/connect/notification');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/notification-member/' +  $("#user-userId").val(), function (data) {
            const responseData = JSON.parse(data.body);

            makeNotification(responseData);
            showNotification();
        });
    });
}

function formatDate(date) {
    const now = new Date();
    const diff = now - date;

    const diffSeconds = Math.floor(diff / 1000);
    const diffMinutes = Math.floor(diffSeconds / 60);
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffMinutes < 1) {
        return '방금 전';
    } else if (diffMinutes < 60) {
        return diffMinutes + '분 전';
    } else if (diffHours < 24) {
        return diffHours + '시간 전';
    } else {
        return diffDays + '일 전';
    }
}

function makeNotification(notification) {
    const createdTime = new Date(notification.createdTime);
    const formattedTime = formatDate(createdTime);

    // 읽지 않은 알림인지 확인
    const isWatched = notification.watched;

    // 각 알림에 대한 HTML 생성
    const notificationHTML = `
                    <div class="notification ${!isWatched ? 'unwatched' : ''}">
                        <div class="notification-title">${notification.title}</div>
                        <div class="notification-body"></div>
                        <div class="notification-time-delete-wrapper">
                            <div class="notification-time">${formattedTime}</div>
                            <div>
                                ${!isWatched ? '<button class="watch-btn" onclick="markAsRead(this, ' + notification.notificationId + ')">읽음</button>' : ''}
                                <button class="delete-btn" onclick="deleteNotification(this, ${notification.notificationId})">삭제</button>
                            </div>
                        </div>
                    </div>
                `;

    // notification-list에 새로운 알림 추가
    const $notificationElement = $(notificationHTML);
    $notificationElement.find('.notification-body').html(notification.contents); // HTML 콘텐츠 삽입
    $('.notification-list').append($notificationElement);
}

function deleteNotification(button, notificationId) {
    $.ajax({
        url: '/notification/' + notificationId + '/delete',
        type: 'DELETE',
        success: function () {
            const notification = button.closest('.notification');
            notification.remove();
        },
        error: function (xhr, status, error) {
            console.error('Error marking notification as delete:', error);
        }
    });
}

function markAsRead(button, notificationId) {
    $.ajax({
        url: '/notification/' + notificationId + '/read',
        type: 'POST',
        success: function () {
            const $notification = $(button).closest('.notification');
            $notification.removeClass('unwatched');
            $notification.find('.watch-btn').remove();
        },
        error: function (xhr, status, error) {
            console.error('Error marking notification as read:', error);
        }
    });
}