//=== 삭제 confirm 모달 ===
var deleteReviewId;

function showDeleteModal(button) {
    deleteReviewId = parseInt(button.getAttribute('data-review-id'));
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
}

//=== 리뷰 삭제 ===
$(document).ready(function() {
    $('#deleteReviewBtn').click(function() {
        $.ajax({
            url: '/my/review/delete',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                reviewId: deleteReviewId
            }),
            success: function(response) {
                const deleteConfirmModal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
                deleteConfirmModal.hide();

                const deleteCompleteModal = new bootstrap.Modal(document.getElementById('deleteCompleteModal'));
                deleteCompleteModal.show();
            },
            error: function(xhr, status, error) {
                alert('리뷰 삭제 중 오류가 발생했습니다.')
            }
        });
    });
});