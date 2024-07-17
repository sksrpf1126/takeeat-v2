//=== marketStatus 값에 따라 checkbox에 checked 속성 부여 ===
var checked = false;
var before;
if (marketStatus == 'OPEN') {
    checked = true;
    before = '영업중';
} else {
    before = '준비중';
}

document.addEventListener('DOMContentLoaded', function() {
    var checkbox = document.getElementById('switch');
    checkbox.checked = checked;
});

//=== marketStatus 변경 confirm 모달 ===
var isChecked;
document.getElementById('updateStatusBtn').addEventListener('click', function () {
    isChecked = document.getElementById('switch').checked;
    var after;
    if (isChecked) {
        after = '영업중';
    } else {
        after = '준비중';
    }

    const message = document.getElementById('message');
    const status = document.getElementById('status');
    message.textContent = '가게 상태를 변경하시겠습니까?';
    status.textContent = before + " → " + after;
    $('#updateStatusModal').modal('show');
});

//=== 모달 취소 버튼 ===
document.getElementById('cancel').addEventListener('click', function () {
    $('#updateStatusModal').modal('hide');
    document.getElementById('switch').checked = checked;
});

//=== 모달 확인 버튼 ===
document.getElementById('update').addEventListener('click', function () {
    $.ajax({
        url: '/market/status/update',
        method: 'POST',
        data: JSON.stringify({
            isChecked: isChecked
        }),
        contentType: 'application/json',
        success: function(response) {
            alert('상태를 변경하였습니다');
            location.reload();
        },
        error: function(error) {
            alert('상태 변경을 실패했습니다');
        }
    });
});