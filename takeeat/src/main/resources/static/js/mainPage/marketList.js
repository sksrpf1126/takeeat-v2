window.onload = function() {
    kakao.maps.load(function() {
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div
            mapOption = {
                center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
                level: 4 // 지도의 확대 레벨
            };

        var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


        var imageSrc = '/images/red-dot.png', // 마커이미지의 주소입니다
            imageSize = new kakao.maps.Size(20, 20), // 마커이미지의 크기입니다
            imageOption = {offset: new kakao.maps.Point(10, 10)}; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.

        // 사용자의 위치 마커의 이미지정보를 가지고 있는 마커이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption),
            markerPosition = new kakao.maps.LatLng(latitude, longitude); // 마커가 표시될 위치입니다

        // 사용자의 위치 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImage // 마커이미지 설정
        });

        // 사용자의 위치 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);

        var points = [];

        marketCoordinates.forEach(function(coords) {
            var markerPosition = new kakao.maps.LatLng(coords.latitude, coords.longitude); // 마커가 표시될 위치

            var marketMarker = new kakao.maps.Marker({
                position: markerPosition
            });

            marketMarker.setMap(map); // 마커를 지도에 표시
        });

    });
}

function redirectToSelected() {
    var selectElement = document.getElementById("marketDropdown");
    var selectedValue = selectElement.value;

    // 선택된 값에 따라 URL을 설정합니다.
    var url;
    if (selectedValue === "distance") {
      url = `/${marketCategory}/list?sort=distance`;
    } else if (selectedValue === "review") {
      url = `/${marketCategory}/list?sort=review`;
    } else if (selectedValue === "score") {
      url = `/${marketCategory}/list?sort=score`;
    }

    // URL로 이동합니다.
    if (url) {
      window.location.href = url;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // 현재 URL에서 경로를 추출하여 선택된 카테고리를 결정합니다.
    var path = window.location.pathname;
    var categoryLinks = document.querySelectorAll('.list li a');

    // 각 링크 요소에 대해 현재 URL 경로와 일치하는 링크에 strong 태그를 추가합니다.
    categoryLinks.forEach(link => {
        if (link.getAttribute('href') === path) {
            link.innerHTML = '<strong>' + link.innerHTML + '</strong>';
        }
    });
});