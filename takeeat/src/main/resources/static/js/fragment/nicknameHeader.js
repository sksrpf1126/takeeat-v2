$(document).ready(function() {
    getNickname();
})

function getNickname() {
    $.ajax({
        url: '/member/nickname',
        type: 'GET',
        success: function(response) {
            const nicknameElement = document.querySelector('.H-nickNameBtn strong');
            nicknameElement.textContent = response + ' 님';
        },
        error: function(xhr, status, error) {
            alert('닉네임 불러오기 실패');
        }
    });
}