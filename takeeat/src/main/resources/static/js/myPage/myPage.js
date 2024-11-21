$("#delete-member").on("click", function () {
    if (confirm("정말로 회원탈퇴를 하시겠습니까?")) {
        $.ajax({
            url: "/member/delete",
            type: "DELETE",
            contentType: "application/json",
            success: function (response) {
                alert("회원탈퇴가 완료되었습니다.");
                window.location.href = "/";
            },
            error: function (xhr, status, error) {
                alert("회원탈퇴에 실패하였습니다: " + xhr.responseText);
                console.error("Error:", error);
            }
        });
    } else {
        alert("회원탈퇴가 취소되었습니다.");
    }
});