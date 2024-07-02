//==== 탭 메뉴 ====
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

function showInfoTab() {
    setTimeout(function() {
        relayout();
    }, 100); // 100ms 정도 지연을 추가하여 영역이 완전히 렌더링되도록 합니다.
}

function relayout() {
    map.relayout();
    map.setCenter(new kakao.maps.LatLng(latitude, longitude));
}

//==== 옵션 선택 모달 보여주기 ====
function showOptionModal(menuId) {
    const modal = new bootstrap.Modal(document.getElementById('optionChoiceModal' + menuId));
    modal.show();
}

$(document).ready(function() {
    //=== 옵션 선택 모달을 닫으면 모달 내의 데이터 초기화 ===
    $('.modal').on('hide.bs.modal', function() {
        $(this).find('input[type="checkbox"]').prop('checked', false);

        $(this).find('input[type="radio"]').each(function() {
            var groupName = $(this).attr('name');
            $(this).closest('.modal').find('input[name="' + groupName + '"]').first().prop('checked', true);
        });

        $(this).find('input[name="quantity"]').val(1);

        var basePrice = parseInt($(this).find('.totalPrice').data('base-price'));
        $(this).find('.totalPrice strong').text(basePrice.toLocaleString() + '원');
    });

    //=== checkbox 최대 선택 개수 체크 ===
    $(document).on('change', 'input[type="checkbox"]', function() {
        var maxCount = $(this).data('max-count');
        var groupName = $(this).attr('name');
        var checkedCount = $('input[name="' + groupName + '"]:checked').length;

        if (checkedCount > maxCount) {
            $(this).prop('checked', false);
            alert(maxCount + '개까지 선택 가능합니다.');
        }
    });

    //=== totalPrice 계산 ===
    $(document).on('change', 'input[type="radio"], input[type="checkbox"]', function() {
        updateTotalPrice($(this).closest('.modal'));
    });

    window.stepUpAndUpdate = function(button) {
        var input = button.parentNode.querySelector('input[type=number]');
        input.stepUp();
        updateTotalPrice($(button).closest('.modal'));
    }

    window.stepDownAndUpdate = function(button) {
        var input = button.parentNode.querySelector('input[type=number]');
        input.stepDown();
        updateTotalPrice($(button).closest('.modal'));
    }

    function updateTotalPrice(modal) {
        var basePrice = parseInt($(modal).find('.totalPrice').data('base-price'));
        var totalPrice = basePrice;

        $(modal).find('input[type="checkbox"]:checked, input[type="radio"]:checked').each(function() {
            var optionPrice = parseInt($(this).data('price'));
            totalPrice += optionPrice;
        });

        var quantity = parseInt($(modal).find('input[name="quantity"]').val());
        totalPrice *= quantity;

        $(modal).find('.totalPrice strong').text(totalPrice.toLocaleString() + '원');
    }
});