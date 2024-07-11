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

var reportReviewId;

//=== 신고 confirm 모달 ===
function showReportModal(button) {
    reportReviewId = parseInt(button.getAttribute('data-review-id'));
    const modal = new bootstrap.Modal(document.getElementById('confirmReportModal'));
    modal.show();
}

$(document).ready(function() {
    //=== 사장님 답글 작성/수정/삭제 ===
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

    //=== 리뷰 신고 ===
    $('#reportReviewBtn').click(function() {
        $.ajax({
            url: '/market/review/report',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                reviewId: reportReviewId
            }),
            success: function(response) {
                const confirmReportModal = bootstrap.Modal.getInstance(document.getElementById('confirmReportModal'));
                confirmReportModal.hide();

                const reportCompleteModal = new bootstrap.Modal(document.getElementById('reportCompleteModal'));
                reportCompleteModal.show();
            },
            error: function(xhr, status, error) {
                alert('리뷰 신고 중 오류가 발생했습니다.')
            }
        });
    });
});