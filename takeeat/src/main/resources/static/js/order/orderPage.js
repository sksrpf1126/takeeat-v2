//==== 지도 api ====
var map = null;

window.onload = function() {
    kakao.maps.load(function() {
        var mapContainer = document.getElementById('map'), // 지도를 표시할 div
            mapOption = {
                center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };

        map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        // 마커가 표시될 위치입니다
        var markerPosition  = new kakao.maps.LatLng(latitude, longitude);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition
        });

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);
    });
}

function showMap() {
    setTimeout(function() {
        relayout();
    }, 100); // 100ms 정도 지연을 추가하여 영역이 완전히 렌더링되도록 합니다.
}

function relayout() {
    map.relayout();
    map.setCenter(new kakao.maps.LatLng(latitude, longitude));
}

document.addEventListener('DOMContentLoaded', function() {
    //=== 옵션 가격 계산 후 출력 ===
    var optionAndPriceElements = document.querySelectorAll('.optionAndPrice');
    optionAndPriceElements.forEach(function(element) {
        var totalOptionPrice = 0;

        var optionPriceElements = element.querySelectorAll('.optionPrice');
        optionPriceElements.forEach(function(priceElement) {
            var price = parseInt(priceElement.getAttribute('data-option-price'));
            totalOptionPrice += price;
        });

        if (totalOptionPrice > 0) {
            var totalOptionPriceElement = element.querySelector('.totalOptionPrice');
            totalOptionPriceElement.textContent = '+' + totalOptionPrice.toLocaleString() + '원';
        }
    });

    //=== 총 주문금액 계산 후 출력 ===
    var menuContainerElements = document.querySelectorAll('.menuContainer');
    var totalPrice = 0;
    menuContainerElements.forEach(function(element) {
        var totalMenuPriceElement = element.querySelector('.totalMenuPrice');

        var price = totalMenuPriceElement.getAttribute('data-menu-price');
        var quantity = totalMenuPriceElement.getAttribute('data-menu-quantity');

        totalPrice += (price * quantity);
    });

    var totalPriceElement = document.getElementById('totalPrice');
    totalPriceElement.textContent = totalPrice.toLocaleString() + '원';

    var orderBtnContentElement = document.getElementById('orderBtnContent');
    orderBtnContentElement.textContent = totalPrice.toLocaleString() + '원 결제하기';

    //=== 지도 toggle ===
    var mapBtn = document.querySelector('.mapBtn');
    var mapElement = document.getElementById('map');

    mapBtn.addEventListener('click', function() {
        toggleMapDisplay();
    });

    function toggleMapDisplay() {
        if (mapElement.style.display == 'none') {
            mapElement.style.display = 'block';
            showMap();
        } else {
            mapElement.style.display = 'none';
        }
    }
});