window.onload = function () {
    const checkAll = document.getElementById('chkAll');
    const chks = document.querySelectorAll('.chk');
    const chkBoxLength = chks.length;
    const agreeBtn = document.getElementById('agreeBtn');
    const joinForm = document.getElementById('joinForm');
    const emailInput = document.getElementById('email');
    const providerTypeInput = document.getElementById('providerType');

    checkAll.addEventListener('click', function(event) {
        if(event.target.checked) {
            chks.forEach(function(value){
                value.checked = true;
            });
            agreeBtn.disabled = false; // 모든 체크박스가 체크되면 동의 버튼 활성화
        } else {
            chks.forEach(function(value){
                value.checked = false;
            });
            agreeBtn.disabled = true; // 체크박스가 모두 체크 해제되면 동의 버튼 비활성화
        }
    });

    chks.forEach(chk => {
        chk.addEventListener('click', function() {
            let count = 0;
            chks.forEach(function(value){
                if(value.checked == true){
                    count++;
                }
            });
            if(count !== chkBoxLength){
                checkAll.checked = false;
                agreeBtn.disabled = true; // 모든 체크박스가 선택되지 않았으면 동의 버튼 비활성화
            } else {
                checkAll.checked = true;
                agreeBtn.disabled = false; // 모든 체크박스가 선택되면 동의 버튼 활성화
            }
        });
    });

    agreeBtn.addEventListener('click', function() {
        // 이메일과 제공자 타입에 따라 액션 URL을 설정
        if (emailInput.value && providerTypeInput.value) {
            joinForm.action = '/member/social-signup';
        } else {
            joinForm.action = '/member/default-signup';
        }
        // 동의 버튼을 클릭하면 폼 제출
        joinForm.submit();
    });
}