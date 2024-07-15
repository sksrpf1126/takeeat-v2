// 드롭다운 선택 변경 시 호출되는 함수
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

// 페이지 로드 시 URL 파라미터를 확인하고 드롭다운의 값을 설정하는 함수
function setDropdownValue() {
    var selectElement = document.getElementById("marketDropdown");
    // onchange 이벤트 리스너를 임시로 제거합니다.
    selectElement.onchange = null;

    var urlParams = new URLSearchParams(window.location.search);
    var sort = urlParams.get('sort');
    if (sort) {
        selectElement.value = sort;
    }

    // onchange 이벤트 리스너를 다시 추가합니다.
    selectElement.onchange = redirectToSelected;
}

window.onload = function() {
    setDropdownValue();

    kakao.maps.load(function() {
        var mapContainer = document.getElementById('map');
        var mapOption = {
            center: new kakao.maps.LatLng(latitude, longitude),
            level: 4
        };
        var map = new kakao.maps.Map(mapContainer, mapOption);

        var imageSrc = '/images/red-dot.png';
        var imageSize = new kakao.maps.Size(20, 20);
        var imageOption = { offset: new kakao.maps.Point(10, 10) };
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);

        var userMarkerPosition = new kakao.maps.LatLng(latitude, longitude);
        var userMarker = new kakao.maps.Marker({
            position: userMarkerPosition,
            image: markerImage
        });
        userMarker.setMap(map);

        // 커스텀 오버레이를 닫기 위한 함수
        window.closeOverlay = function(element) {
            var overlay = element.closest('.wrap');
            var customOverlay = overlay.kakaoCustomOverlay;
            if (customOverlay) {
                customOverlay.setMap(null);
            }
        };

        // Function to create a custom overlay content
        function createCustomOverlayContent(response, customOverlay) {
            var content = '<div class="wrap" kakaoCustomOverlay>' +
                          '    <div class="info">' +
                          '        <div class="title" onclick="location.href=\'/' + response.marketId + '/menu\'">' + response.marketName + '</div>' +
                      '            <div class="close" onclick="closeOverlay(this)" title="닫기"></div>' +
                          '        <div class="body">' +
                          '            <div class="img">' +
                          '                <img src="' + response.marketImage + '" width="73" height="70">' +
                          '            </div>' +
                          '            <div class="desc">' +
                          '                <div class="ellipsis">' + response.query + '</div>' +
                          '                <div class="jibun ellipsis">(' + response.addressDetail + ')</div>' +
                          '                <div class="jibun ellipsis">평점 : ' + response.marketRating + ' 리뷰 : ' + response.reviewCount + '</div>' +
                          '            </div>' +
                          '        </div>' +
                          '    </div>' +
                          '</div>';

            var tempDiv = document.createElement('div');
            tempDiv.innerHTML = content.trim();
            var overlayElement = tempDiv.firstChild;
            overlayElement.kakaoCustomOverlay = customOverlay;

            return overlayElement;
        }

        // marketInfoResponse 배열에서 각각의 정보를 이용하여 마커와 커스텀 오버레이를 생성합니다.
        marketInfoResponse.forEach(function(response) {
            var markerPosition = new kakao.maps.LatLng(response.latitude, response.longitude);
            var marketMarker = new kakao.maps.Marker({
                position: markerPosition
            });
            marketMarker.setMap(map);

            var customOverlay = new kakao.maps.CustomOverlay({
                position: markerPosition,
                clickable: true
            });

            var overlayContent = createCustomOverlayContent(response, customOverlay);
            customOverlay.setContent(overlayContent);

            // 마커를 클릭하면 커스텀 오버레이를 지도에 표시합니다.
            kakao.maps.event.addListener(marketMarker, 'click', function() {
                customOverlay.setMap(map);
            });

            // 지도를 클릭하면 커스텀 오버레이를 닫습니다.
            kakao.maps.event.addListener(map, 'click', function() {
                customOverlay.setMap(null);
            });
        });
    });
};

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