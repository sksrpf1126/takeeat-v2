$("#delete-member").on("click", function () {
    if (confirm("정말로 회원탈퇴를 하시겠습니까?")) {
        $.ajax({
            url: "/member/delete",
            type: "DELETE",
            contentType: "application/json",
            success: function (response) {
                alert("회원탈퇴가 완료되었습니다.");
                window.location.href = "/member/logout";
            },
            error: function (xhr, status, error) {
                const responseText = JSON.parse(xhr.responseText);

                if(responseText.code === "U_010") {
                    alert(
                        "소셜 정보가 만료되었습니다. \n"
                        +
                        "회원탈퇴를 원하시면 로그아웃 후 다시 로그인해주세요."
                    );
                }else {
                    alert(responseText.message);
                }
            }
        });
    } else {
        alert("회원탈퇴가 취소되었습니다.");
    }
});