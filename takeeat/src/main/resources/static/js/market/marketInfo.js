// 드롭다운 카테고리 함수
document.getElementById('dropdownButton').addEventListener('click', function() {
    document.getElementById('dropdownContent').classList.toggle('show');
});

function selectCategory(category) {
    $('#category-error').hide();
    document.getElementById('dropdownButton').textContent = category;
    document.getElementById('marketCategory').value = category;
    document.getElementById('dropdownContent').classList.remove('show');
}

/*window.onclick = function(event) {
    if (!event.target.matches('.dropdown-button')) {
        var dropdowns = document.getElementsByClassName('dropdown-content');
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}*/

// 이미지 미리보기 함수
document.getElementById('input-file').addEventListener('change', function(event) {
    var input = event.target;
    var reader = new FileReader();

    reader.onload = function(){
        var dataURL = reader.result;
        var imgPreview = document.getElementById('img-preview');
        imgPreview.src = dataURL;
    };

    if (input.files && input.files[0]) {
        $('#image-error').hide();
        reader.readAsDataURL(input.files[0]);
    }
});

 // 우편번호 찾기 찾기 화면을 넣을 element
    var element_wrap = document.getElementById('wrap');

    function foldDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_wrap.style.display = 'none';
    }


var geocoder;

// Kakao Maps API 로드 후 실행될 함수
function initMap() {
    kakao.maps.load(function() {
        geocoder = new kakao.maps.services.Geocoder();
    });
}

// window.onload를 통해 Kakao Maps API 로드 후 initMap 함수 호출
window.onload = function() {
    initMap();
};

// 주소 검색 버튼 클릭 이벤트 설정
document.getElementById('sample3_searchButton').addEventListener('click', function() {
    sample3_execDaumPostcode();
});

// 주소찾기 함수 수정
function sample3_execDaumPostcode() {
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            // geocoder 객체가 정상적으로 초기화되었는지 체크
            if (geocoder) {
                geocoder.addressSearch(addr, getCoord);
            } else {
                console.error('geocoder 객체가 초기화되지 않았습니다.');
            }

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                document.getElementById("sample3_extraAddress").value = extraAddr;
            } else {
                document.getElementById("sample3_extraAddress").value = '';
            }

            $('#query-error').hide();
            document.getElementById('sample3_postcode').value = data.zonecode;
            document.getElementById("sample3_address").value = addr;
            document.getElementById("sample3_detailAddress").focus();

            element_wrap.style.display = 'none';
            document.body.scrollTop = currentScroll;
        },
        onresize: function(size) {
            element_wrap.style.height = size.height + 'px';
        },
        width: '100%',
        height: '100%'
    }).embed(element_wrap);

    element_wrap.style.display = 'block';
}

// 좌표 결과 처리 함수
function getCoord(status, result) {
    if (status === kakao.maps.services.Status.OK) {
        console.log('lat' + result[0].road_address.y);
        console.log('long' + result[0].road_address.x);
    } else {
        alert('좌표를 읽어올 수 없습니다');
    }
}

// 중복검사 ajax
$(document).ready(function() {
    $('.dup-button').on('click', function() {

        var baseUrl = window.location.origin;

        $.ajax({
            url: baseUrl + '/market/marketName/check',
            type: 'GET',
            data: {
                marketName: $('#marketName').val()
            },
            success: function(isAvailable) {
                if (isAvailable === false) {  // explicitly check for true
                    $('#nameNotAvailable').hide();
                    $('#not-blank').hide();
                    $('#nameAvailable').show().text('사용 가능한 이름입니다.');
                } else if (isAvailable === true) {  // explicitly check for false
                    $('#nameAvailable').hide();
                    $('#nameNotAvailable').show().text('이미 사용중인 이름입니다.');
                } else {
                    $('#nameAvailable').hide();
                    $('#nameNotAvailable').show().text('응답 오류: ' + isAvailable);
                }
            },
            error: function() {
                $('#nameAvailable').hide();
                $('#nameNotAvailable').show().text('오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
});

// 전화번호, 사업자번호 창 입력시 에러 해제
document.addEventListener('DOMContentLoaded', function () {
    var businessNumberInput = document.getElementById('businessNumber');
    var businessNumberError = document.getElementById('businessNumberError');
    var marketNumberInput = document.getElementById('marketNumber');
    var marketNumberError = document.getElementById('marketNumberError');

    function validateInput(input, errorElement) {
        if (input.value.trim() === '') {
            errorElement.style.display = 'block';
        } else {
            errorElement.style.display = 'none';
        }
    }

    businessNumberInput.addEventListener('input', function () {
        validateInput(businessNumberInput, businessNumberError);
    });

    marketNumberInput.addEventListener('input', function () {
        validateInput(marketNumberInput, marketNumberError);
    });

});

// 전화번호 입력시 숫자만 입력하도록 제한
function validatePhoneNumber(event) {
    const input = event.target;
    input.value = input.value.replace(/[^0-9]/g, ''); // 숫자만 남기기
}