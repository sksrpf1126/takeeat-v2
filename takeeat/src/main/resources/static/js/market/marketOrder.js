const pathname = window.location.pathname;
const pathParts = pathname.split('/');
const marketId = pathParts[2]; // URL 형식: /market/{marketId}/order 라고 가정
let sortOrder = 'latest';

function showTab(tabId) {
    // 사이드바 탭 활성화
    $('.sidebar li').removeClass('active');
    $('.sidebar li[onclick="showTab(\'' + tabId + '\')"]').addClass('active');

    // AJAX 요청을 통해 데이터 가져오기
    let status;
    if (tabId === 'pending') {
        status = 'WAIT';
    } else if (tabId === 'processing') {
        status = 'ACCEPT';
    } else if (tabId === 'completed') {
        status = 'COMPLETE';
    }

    if (status) {

        $.ajax({
            url:  /market/ + marketId + '/orders',
            type: 'GET',
            data: {orderStatus: status, sortOrder: sortOrder},
            success: function (data) {
                updateOrderList(data);
                $('#order-item-detail-div').html('<div class="order-not-data-string">조회된 주문이 존재하지 않습니다.</div>');

            },
            error: function (xhr, status, error) {
                console.error('Error fetching order data:', error);
            }
        });
    }
}

function updateOrderList(data) {
    const orderList = $('.order-list');
    orderList.empty();

    data.forEach(function (order) {
        const orderItem = $('<div>', {
            class: 'order-item',
            'data-order-id': order.orderId
        });

        const topDiv = $('<div>', {
            class: 'order-item-top-div',
            text: new Date(order.orderCreateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false })
        });

        const bottomDiv = $('<div>', { class: 'order-item-bottom-div' });
        const menuDiv = $('<div>', { text: '[메뉴 ' + order.menuCount + '개]' });
        const priceDiv = $('<div>', { text: order.totalPrice + '원' });

        bottomDiv.append(menuDiv).append(priceDiv);
        orderItem.append(topDiv).append(bottomDiv);

        orderList.append(orderItem);
    });


    $('.order-item').click(function () {
        const orderId = $(this).data('order-id');

        $('.order-item').removeClass('active-background');


        $(this).addClass('active-background');
        fetchOrderDetails(orderId);
    });
}

function fetchOrderDetails(orderId) {
    $.ajax({
        url: '/market/order/' + orderId,
        type: 'GET',
        success: function (data) {
            renderOrderDetails(data, orderId);
        },
        error: function (xhr, status, error) {
            alert(xhr.responseJSON.message);
        }
    });
}

// 주문 상세 데이터를 화면에 렌더링하는 함수
function renderOrderDetails(data, orderId) {
    var orderDetailsHtml = `
        <div id="order-item-detail-header-div">
            <div id="order-item-title">포장 주문: ${new Date(data.orderCreateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</div>
            <div id="order-item-sub-title">
                <p>[메뉴 ${data.menuCount}개]</p>
                <p>&nbsp;&nbsp;-&nbsp;&nbsp;</p>
                <p>${data.totalPrice.toLocaleString()}원</p>
            </div>
        </div>
        <div id="order-item-detail-contents-div">
            <div id="order-item-detail-contents-left">
                <div id="order-item-requirement-div" class="order-item-detail-border">
                    <h4>요청사항</h4>
                    <p>${data.requirement}</p>
                </div>
                <div id="order-item-information-div" class="order-item-detail-border">
                    <h4>주문내역</h4>
                    <div id="order-menu-list-div">
    `;

    data.orderMenuResponses.forEach(function(orderMenu) {
        var totalMenuPrice = orderMenu.menuPrice * orderMenu.orderQuantity;
        var orderMenuDiv = `
            <div class="order-menu-div">
                <div class="order-menu-main-div">
                    <p class="menu-name">${orderMenu.menuName}</p>
                    <p class="menu-quantity">${orderMenu.orderQuantity}</p>
                    <p class="menu-price">${totalMenuPrice.toLocaleString()}원</p>
                </div>
                <div class="order-menu-option-div">
        `;

        orderMenu.orderOptionResponses.forEach(function(option) {
            orderMenuDiv += `<div>+ ${option.optionName} (${option.optionPrice.toLocaleString()}원)</div>`;
        });

        orderMenuDiv += `</div></div>`;
        orderDetailsHtml += orderMenuDiv;
    });

    orderDetailsHtml += `
                    </div>
                    <div id="order-menu-list-print">주문전표 출력</div>
                </div>
            </div>
            <div id="order-item-detail-contents-right">
                <div id="order-detail-information-div" class="order-item-detail-border">
                    <div id="member-phone-div">
                        <h4>고객연락처</h4>
                        <p id="member-phone-number">${data.phone}</p>
                        <p class="safe-phone-string">안심번호는 주문접수 후 최대 N시간 동안 유효합니다.</p>
                    </div>
                    <div>
                        <div class="order-sub-info-div">
                            <h4>주문번호</h4>
                            <p>ABCDEFGH1234</p>
                        </div>
                        <div class="order-sub-info-div">
                            <h4>주문시간</h4>
                            <p>${new Date(data.orderCreateTime).toLocaleString()}</p>
                        </div>
                        <div class="order-sub-info-div">
                            <h4>접수시간</h4>
                            <p></p>
                        </div>
                        <div class="order-sub-info-div">
                            <h4>완료시간</h4>
                            <p></p>
                        </div>
                    </div>
                </div>
                <div id="market-select-div">
                    ${renderButtonsByStatus(data.orderStatus)}
                </div>
            </div>
        </div>
    `;

    $('#order-item-detail-div').html(orderDetailsHtml);

    if(data.orderStatus == 'WAIT') {
        const decreaseButton = document.getElementById("decrease");
        const increaseButton = document.getElementById("increase");
        const timeInput = document.getElementById("timeInput");
        const rejectButton = document.getElementById("reject");

        if(rejectButton) rejectButton.addEventListener("click", function() {
            handleRejectClick(orderId, data.memberId, data.orderStatus, );
        });

        decreaseButton.addEventListener("click", function() {
            updateTime(-1);
        });

        increaseButton.addEventListener("click", function() {
            updateTime(1);
        });

        timeInput.addEventListener("click", function() {
            let newTime = prompt("새 시간을 입력하세요 (분):", parseInt(timeInput.textContent));
            if (newTime !== null) {
                newTime = parseInt(newTime);
                if (!isNaN(newTime) && newTime >= 0) {
                    timeInput.textContent = newTime + "분";
                } else {
                    alert("유효한 숫자를 입력하세요.");
                }
            }
        });
    }

}

// 주문 상태에 따라 버튼 구성 변경
function renderButtonsByStatus(status) {
    if (status === "WAIT") {
        return `
            <button class="action-button" id="reject">거부</button>
            <div class="time-control">
                <button class="control-button" id="decrease">-</button>
                <p id="timeInput">60분</p>
                <button class="control-button" id="increase">+</button>
            </div>
            <button class="action-button" id="accept">접수</button>
        `;
    } else if (status === "ACCEPT") {
        return `
            <button class="action-button" id="cancel">취소</button>
            <button class="action-button" id="complete">완료처리</button>
        `;
    } else {
        return '';
    }
}

function updateTime(amount) {
    let currentTime = parseInt(timeInput.textContent);
    currentTime += amount;
    if (currentTime < 0) {
        currentTime = 0;
    }
    timeInput.textContent = currentTime + "분";
}

// 정렬 순서 변경 이벤트 핸들러
$('.order-select-condition-div div').click(function () {
    $('.order-select-condition-div div').removeClass('active');
    $(this).addClass('active');
    sortOrder = $(this).attr('id') === 'latest' ? 'latest' : 'oldest';
    // 선택된 탭 다시 로드
    const activeTab = $('.sidebar li.active').attr('onclick').match(/showTab\('(\w+)'\)/)[1];
    showTab(activeTab);
});

showTab('pending');
