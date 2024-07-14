$("#input-file-button").on('change',function(){
  var fileName = $("#input-file-button").val();
  $(".file-style").val(fileName);
});

document.getElementById('input-file').addEventListener('change', function(event) {
    var input = event.target;
    var imgPreview = document.getElementById('img-preview');

    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            imgPreview.src = e.target.result;
        };

        // 파일을 읽어와서 URL을 생성하여 이미지 요소의 src에 할당
        reader.readAsDataURL(input.files[0]);

        // FileReader 대신 URL.createObjectURL을 사용하여도 됩니다.
        // imgPreview.src = URL.createObjectURL(input.files[0]);

        $('#image-error').hide();
    }
});

// AJAX를 이용한 중복 검사
$(document).ready(function() {
    $('#name-valid').on('click', function() {
        var baseUrl = window.location.origin;

        $.ajax({
            url: baseUrl + '/market/marketName/check',
            type: 'GET',
            data: {
                marketName: $('#marketName').val()
            },
            success: function(isAvailable) {
                if (isAvailable === false) {
                    $('#nameNotAvailable').hide();
                    $('#not-blank').hide();
                    $('#nameAvailable').show().text('사용 가능한 이름입니다.');
                } else if (isAvailable === true) {
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


var element_wrap = document.getElementById('wrap');
var geocoder;

function foldDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_wrap.style.display = 'none';
}

function sample6_execDaumPostcode() {
    // 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function(data) {
        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
        var addr = ''; // 주소 변수
        var extraAddr = ''; // 참고항목 변수

        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
            addr = data.roadAddress;
        } else { // 사용자가 지번 주소를 선택했을 경우(J)
            addr = data.jibunAddress;
        }

        // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
        if(data.userSelectedType === 'R'){
            // 법정동명이 있을 경우 추가한다. (법정리는 제외)
            // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                extraAddr += data.bname;
            }
            // 건물명이 있고, 공동주택일 경우 추가한다.
            if(data.buildingName !== '' && data.apartment === 'Y'){
                extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
            if(extraAddr !== ''){
                extraAddr = ' (' + extraAddr + ')';
            }
            // 조합된 참고항목을 해당 필드에 넣는다.
            document.getElementById("sample6_extraAddress").value = extraAddr;
        } else {
            document.getElementById("sample6_extraAddress").value = '';
        }

        // 우편번호와 주소 정보를 해당 필드에 넣는다.
        document.getElementById('sample6_postcode').value = data.zonecode;
        document.getElementById("sample6_address").value = addr;

        // 주소 정보를 좌표로 변환한다.
        geocoder.addressSearch(addr, getCoord);

        // iframe을 넣은 element를 안보이게 한다.
        // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
        element_wrap.style.display = 'none';

        // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
        document.body.scrollTop = currentScroll;

        // 커서를 상세주소 필드로 이동한다.
        document.getElementById("sample6_detailAddress").focus();

        /*kakao.maps.load(function () {
            // 주소-좌표 변환 객체를 생성합니다
            var geocoder = new kakao.maps.services.Geocoder();

            // 주소로 좌표를 검색합니다
            geocoder.addressSearch(addr, function(result, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === kakao.maps.services.Status.OK) {
                    var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                    var message = 'latlng: new kakao.maps.LatLng(' + result[0].y + ', ';
                    message += result[0].x + ')';
                    console.log(message);
                }
            })
        })*/
    },
    width : '100%',
    height : '100%'
    }).embed(element_wrap);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap.style.display = 'block';
}

//=== 카카오 맵 API Geocoder ===
window.onload = function() {
    kakao.maps.load(function() {
        geocoder = new kakao.maps.services.Geocoder();
    });
}

//=== 주소 -> 좌표 ===
function getCoord(result, status) {
    if (status === kakao.maps.services.Status.OK) {
        latitude = result[0].road_address.y;
        longitude = result[0].road_address.x;
        document.getElementById("latitude").value =latitude;
        document.getElementById("longitude").value =longitude;
        console.log("latitude="+latitude);
        console.log("longitude="+longitude);
    } else {
        alert('좌표를 읽어올 수 없습니다');
    }
}



/*// 전화번호, 사업자번호 창 입력시 에러 해제
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


});*/

