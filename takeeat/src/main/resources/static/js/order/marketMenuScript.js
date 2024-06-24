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