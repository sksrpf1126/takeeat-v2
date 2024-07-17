const pathname = window.location.pathname;
const pathParts = pathname.split('/');
const marketId = pathParts[2]; // URL 형식: /market/{marketId}/order 라고 가정
let sortOrder = 'latest';
let status = 'WAIT';

function showTab(tabId) {
    // 사이드바 탭 활성화
    $('.order-sidebar li').removeClass('active');
    $('.order-sidebar li[onclick="showTab(\'' + tabId + '\')"]').addClass('active');

    // AJAX 요청을 통해 데이터 가져오기
    if (tabId === 'pending') {
        status = 'WAIT';
    } else if (tabId === 'processing') {
        status = 'ACCEPT';
    } else if (tabId === 'completed') {
        status = 'COMPLETE';
    } else if(tabId === 'list-select') {
        status = 'LIST_SELECT';
    }

    if (status && (status !== 'LIST_SELECT')) {
        renderSearchFormNotSearchList();
        fetchOrderList(marketId, status, sortOrder);
    }else if(status === 'LIST_SELECT') {
        renderSearchForm();
    }
    const searrcVH = getElementHeightInVH('.order-select-condition-div');
    orderListHeightVH = 94 - searrcVH;
    document.querySelector('.order-list').style.height = `${orderListHeightVH}vh`;
}

function renderSearchFormNotSearchList() {
    const searchFormHtml = `
        <div id="latest" class="${sortOrder === 'latest' ? 'active' : ''}">최신순</div>
        <div id="oldest" class="${sortOrder === 'oldest' ? 'active' : ''}">과거순</div>
    `;

    $('.order-select-condition-div').html(searchFormHtml);

    // 정렬 순서 변경 이벤트 핸들러
    $('.order-select-condition-div div').off('click').on('click', function () {
        $('.order-select-condition-div div').removeClass('active');
        $(this).addClass('active');
        sortOrder = $(this).attr('id') === 'latest' ? 'latest' : 'oldest';
        // 선택된 탭 다시 로드
        const activeTab = $('.order-sidebar li.active').attr('onclick').match(/showTab\('(\w+)'\)/)[1];
        showTab(activeTab);
    });
}

function renderSearchForm() {
    const date = new Date();
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    const dateStr = `${year}-${month}-${day}`;

    const searchFormHtml = `
    <div id="order-list-search-div">
        <div class="form-group">
            <label class="color-white">주문 상태</label>
            <div id="order-list-checkbox-div">
                <input type="checkbox" id="wait-checkbox" name="orderStatus" value="WAIT" checked>
                <label for="wait-checkbox">대기</label>
                <input type="checkbox" id="reject-checkbox" name="orderStatus" value="REJECT" checked>
                <label for="reject-checkbox">거절</label>
                <input type="checkbox" id="cancel-checkbox" name="orderStatus" value="CANCEL" checked>
                <label for="cancel-checkbox">취소</label>
                <input type="checkbox" id="accept-checkbox" name="orderStatus" value="ACCEPT" checked>
                <label for="accept-checkbox">접수</label>
                <input type="checkbox" id="complete-checkbox" name="orderStatus" value="COMPLETE" checked>
                <label for="complete-checkbox">완료</label>
            </div>
        </div>
        <div class="form-group">
            <label for="date" class="color-white">날짜</label>
            <input type="date" id="date" name="date" value="${dateStr}">
            <button id="order-list-search-btn" type="button" onclick="searchOrders()">검색</button>
        </div>
        <div class="search-order-status-div">
            <div id="latest" class="active" >최신순</div>
            <div id="oldest">과거순</div>
        </div>
    </div>
    `;

    $('.order-select-condition-div').html(searchFormHtml);

    // 정렬 순서 변경 이벤트 핸들러
    $('.search-order-status-div div').off('click').on('click', function () {
        $('.search-order-status-div div').removeClass('active');
        $(this).addClass('active');
        sortOrder = $(this).attr('id') === 'latest' ? 'latest' : 'oldest';
        searchOrders();
    });

    //기본 값으로 검색(초기화)
    searchOrders();
}

function searchOrders() {
    const orderStatuses = Array.from(document.querySelectorAll('input[name="orderStatus"]:checked'))
        .map(checkbox => checkbox.value);
    const date = document.getElementById('date').value;

    fetchOrderWithSearch(marketId, orderStatuses, sortOrder, date);
}

function fetchOrderWithSearch(marketId, orderStatus, sortOrder, date) {
    $.ajax({
        url: `/market/${marketId}/search-orders`,
        type: 'GET',
        data: {
            orderStatuses: orderStatus.join(','),
            sortOrder: sortOrder,
            selectDate: date
        },
        success: function(data) {
            console.log("search-list data  : ", data);
            updateOrderListWithSearch(data);
            $('#order-item-detail-div').html('<div class="order-not-data-string">조회된 주문이 존재하지 않습니다.</div>');
        },
        error: function(xhr, status, error) {
            console.error('Error fetching order data:', error);
        }
    });
}

function fetchOrderList(marketId, orderStatus, sortOrder) {
    $.ajax({
        url:  /market/ + marketId + '/orders',
        type: 'GET',
        data: {orderStatuses: orderStatus, sortOrder: sortOrder},
        success: function (data) {
            updateOrderList(data);
            $('#order-item-detail-div').html('<div class="order-not-data-string">조회된 주문이 존재하지 않습니다.</div>');

        },
        error: function (xhr, status, error) {
            console.error('Error fetching order data:', error);
        }
    });
}

function updateOrderList(data) {
    const orderList = $('.order-list');
    orderList.empty();

    data.forEach(function (order) {
        const orderItem = createOrderItem(order);
        orderList.append(orderItem);
    });

    $('.order-item').click(function () {
        const orderId = $(this).data('order-id');
        $(this).find('.new-badge').remove();
        $('.order-item').removeClass('active-background');
        $(this).addClass('active-background');
        fetchOrderDetails(orderId);
    });
}

function updateOrderListWithSearch(data) {
    const orderList = $('.order-list');
    orderList.empty();

    data.forEach(function (order) {
        const orderItem = createOrderItemWithSearch(order);
        orderList.append(orderItem);
    });

    $('.order-item').click(function () {
        const orderId = $(this).data('order-id');
        $('.order-item').removeClass('active-background');
        $(this).addClass('active-background');
        fetchOrderDetails(orderId);
    });
}

function createOrderItemWithSearch(order) {
    const orderItem = $('<div>', {
        class: 'order-item',
        'data-order-id': order.orderId
    });

    const topDiv = $('<div>', {
        class: 'order-item-top-div',
        text: new Date(order.orderCreateTime).toLocaleString()
    });

    let badgeText = '';
    let badgeClass = '';

    switch(order.orderStatus) {
        case 'WAIT':
            badgeText = '대기중';
            badgeClass = 'badge-wait';
            break;
        case 'REJECT':
            badgeText = '거절됨';
            badgeClass = 'badge-reject';
            break;
        case 'CANCEL':
            badgeText = '취소됨';
            badgeClass = 'badge-cancel';
            break;
        case 'ACCEPT':
            badgeText = '접수됨';
            badgeClass = 'badge-accept';
            break;
        case 'COMPLETE':
            badgeText = '완료됨';
            badgeClass = 'badge-complete';
            break;
        default:
            break;
    }

    if (badgeText) {
        const newBadge = $('<span>', {
            class: 'new-badge',
            text: badgeText
        }).addClass(badgeClass);
        topDiv.append(newBadge);
    }

    const bottomDiv = $('<div>', { class: 'order-item-bottom-div' });
    const menuDiv = $('<div>', { text: '[메뉴 ' + order.menuCount + '개]' });
    const priceDiv = $('<div>', { text: order.totalPrice.toLocaleString() + '원' });

    bottomDiv.append(menuDiv).append(priceDiv);
    orderItem.append(topDiv).append(bottomDiv);

    return orderItem;
}

function createOrderItem(order) {
    const orderItem = $('<div>', {
        class: 'order-item',
        'data-order-id': order.orderId
    });

    const topDiv = $('<div>', {
        class: 'order-item-top-div',
        text: new Date(order.orderCreateTime).toLocaleString()
    });

    if (!order.orderCheck) {
        const newBadge = $('<span>', {
            class: 'new-badge',
            text: 'NEW'
        }).addClass('order-new');
        topDiv.append(newBadge);
    }

    const bottomDiv = $('<div>', { class: 'order-item-bottom-div' });
    const menuDiv = $('<div>', { text: '[메뉴 ' + order.menuCount + '개]' });
    const priceDiv = $('<div>', { text: order.totalPrice.toLocaleString() + '원' });

    bottomDiv.append(menuDiv).append(priceDiv);
    orderItem.append(topDiv).append(bottomDiv);

    return orderItem;
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
                            <p>${data.acceptedTime ? new Date(data.acceptedTime).toLocaleString() : ''}</p>
                        </div>
                        <div class="order-sub-info-div">
                            <h4>완료시간</h4>
                            <p>${data.completedTime ? new Date(data.completedTime).toLocaleString() : ''}</p>
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
        const acceptButton = document.getElementById("accept");

        if(rejectButton) rejectButton.addEventListener("click", function() {
            handleRejectClick(orderId, data.memberId, data.orderStatus);
        });

        if(acceptButton) acceptButton.addEventListener("click", function() {
            handleAcceptClick(orderId, data.memberId, data.orderStatus);
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
    }else if(data.orderStatus == 'ACCEPT') {
        const cancelButton = document.getElementById("cancel");
        const completeButton = document.getElementById("complete");

        if(cancelButton) cancelButton.addEventListener("click", function() {
            handleCancelClick(orderId, data.memberId, data.orderStatus);
        });

        if(completeButton) completeButton.addEventListener("click", function() {
            handleCompleteClick(orderId, data.memberId, data.orderStatus);
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

function getElementHeightInVH(selector) {
    const element = document.querySelector(selector);
    if (!element) {
        console.error(`No element found with selector: ${selector}`);
        return null;
    }

    const elementHeightPx = element.offsetHeight; // 요소의 높이 픽셀값
    const viewportHeight = window.innerHeight; // viewport의 높이 픽셀값

    const elementHeightVH = (elementHeightPx / viewportHeight) * 100; // vh 단위로 변환

    return elementHeightVH;
}

showTab('pending');
