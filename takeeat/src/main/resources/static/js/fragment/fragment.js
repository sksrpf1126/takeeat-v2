document.getElementById('H-markerIcon').addEventListener('click', function() {
    var navbarBottom = document.getElementById('H-navbarBottom');
    if (navbarBottom.style.display === 'block') {
        navbarBottom.style.display = 'none';
    } else {
        navbarBottom.style.display = 'block';
    }
});

//=== 저장할 주소, 위도, 경도값 ===
var addr = null;
var latitude;
var longitude;

var geocoder;

//=== 다음 우편번호 API ===
// 우편번호 찾기 찾기 화면을 넣을 element
var element_wrap = document.getElementById('H-wrap');

function foldDaumPostcode() {
    // iframe을 넣은 element를 안보이게 한다.
    element_wrap.style.display = 'none';
}

function sample3_execDaumPostcode() {
    // 현재 scroll 위치를 저장해놓는다.
    var currentScroll = Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    new daum.Postcode({
        oncomplete: function(data) {
            // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("H-address").value = addr;

            // 주소 정보를 좌표로 변환한다.
            geocoder.addressSearch(addr, getCoord);

            // iframe을 넣은 element를 안보이게 한다.
            // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
            element_wrap.style.display = 'none';

            // 우편번호 찾기 화면이 보이기 이전으로 scroll 위치를 되돌린다.
            document.body.scrollTop = currentScroll;
        },
        width : '100%',
        height : '100%'
    }).embed(element_wrap);

    // iframe을 넣은 element를 보이게 한다.
    element_wrap.style.display = 'block';
}

//=== Geolocation API ===
function askForLocation () {
    if (!navigator.geolocation) {
        alert('위치 제공 서비스를 지원하지 않습니다');
    } else {
        navigator.geolocation.getCurrentPosition(accessToGeo, handleError);
    }
}

function accessToGeo (position) {
    latitude = position.coords.latitude;
    longitude = position.coords.longitude;

    var coord = new kakao.maps.LatLng(latitude, longitude);
    geocoder.coord2Address(coord.getLng(), coord.getLat(), getAddr);
}

function handleError(error) {
    switch (error.code) {
        case error.PERMISSION_DENIED:
            alert("User denied the request for Geolocation.");
            break;
        case error.POSITION_UNAVAILABLE:
            alert("Location information is unavailable.");
            break;
        case error.TIMEOUT:
            alert("The request to get user location timed out.");
            break;
        case error.UNKNOWN_ERROR:
            alert("An unknown error occurred.");
            break;
    }
}

//=== 카카오 맵 API Geocoder ===
window.onload = function() {
    kakao.maps.load(function() {
        geocoder = new kakao.maps.services.Geocoder();
    });
}

//=== 좌표 -> 주소 ===
function getAddr(result, status) {
    if (status === kakao.maps.services.Status.OK) {
        addr = result[0].address.address_name;
        document.getElementById("H-address").value = addr;
    } else {
        alert('주소를 읽어올 수 없습니다');
    }
}

//=== 주소 -> 좌표 ===
function getCoord(result, status) {
    if (status === kakao.maps.services.Status.OK) {
        latitude = result[0].road_address.y;
        longitude = result[0].road_address.x;
    } else {
        alert('좌표를 읽어올 수 없습니다');
    }
}

//=== 가게 리스트 호출 ===
$(document).ready(function() {

    $('#H-searchMarketBtn').click(function() {
        var url = '/0/list';

        if ((sessionAddr != null && addr == null) || (sessionAddr != null && addr != null && addr == sessionAddr)) {
            window.location.href = url;
        } else {
            $.ajax({
                type: 'POST',
                url: "/saveGPSInfo",
                contentType: 'application/json',
                data: JSON.stringify({
                    addr: addr,
                    latitude: latitude,
                    longitude: longitude
                }),
                success: function(response) {
                    window.location.href = url;
                },
                error: function(error) {
                    alert('주소를 입력해주세요.');
                }
            });
        }

    });
});


