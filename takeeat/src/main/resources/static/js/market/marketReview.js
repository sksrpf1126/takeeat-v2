//=== 탭 메뉴 ===
document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.tabs input');
    const contents = document.querySelectorAll('.conbox');

    function showContent() {
        //모든 요소의 active 클래스 제거
        contents.forEach(content => content.classList.remove('active'));

        //체크된 탭에 따라 active 클래스 추가
        if (document.getElementById('tab01').checked) {
            document.querySelector('.con1').classList.add('active');
        } else if (document.getElementById('tab02').checked) {
            document.querySelector('.con2').classList.add('active');
        } else if (document.getElementById('tab03').checked) {
            document.querySelector('.con3').classList.add('active');
        }
    }

    tabs.forEach(tab => tab.addEventListener('change', showContent));
    showContent();
});

//=== 신고 confirm 모달 ===
function showReportModal() {
    const modal = new bootstrap.Modal(document.getElementById('confirmReportModal'));
    modal.show();
}

//=== 사장님 리뷰 작성 ===
$(document).ready(function() {
    $('.ownerReviewWriteBtn').on('click', function() {
        var button = $(this);
        var reviewId = parseInt(button.data('review-id'));
        var textarea = button.closest('.ownerReviewContainer').find('textarea');
        var ownerReviewContent = textarea.val();

        $.ajax({
            url: '/market/review/write',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                reviewId: reviewId,
                ownerReviewContent: ownerReviewContent
            }),
            success: function(response) {
                if (response != 'none')
                    alert(response);
            },
            error: function(xhr, status, error) {
                alert('작업 수행을 실패했습니다');
            }
        });
    });
});