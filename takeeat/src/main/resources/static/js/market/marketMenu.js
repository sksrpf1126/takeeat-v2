let menuCount = 0; // 초기 메뉴 카운트
let categoryCount = 0; // 초기 카테고리 카운트
let hasIncompleteMenuData = false; // 필수 항목 누락 여부 확인 변수
let hasEmptyMenu = false; // 빈 메뉴 여부 확인 변수

window.saveMenu = function() {
    let categories = [];
    const categoryElements = document.querySelectorAll('.category-container');

    let categoryPromises = [];

    // 모든 카테고리와 메뉴 정보 수집
    categoryElements.forEach(categoryItem => {
        let menus = [];  // 해당 카테고리에 포함된 메뉴들을 저장할 배열
        const menuCategoryName = categoryItem.querySelector('.menu-category').value; // 카테고리 이름

        // 카테고리 이름이 비어있으면 경고 메시지 표시
        if (!menuCategoryName) {
            hasIncompleteMenuData = true;
            return;
        }

        const menuElements = categoryItem.querySelectorAll('.menu-item');  // 각 카테고리의 모든 메뉴

        let menuPromises = Array.from(menuElements).map(menuItem => {
            return new Promise((resolve, reject) => {
                const menuName = menuItem.querySelector('.market-menu').value;
                const menuIntroduction = menuItem.querySelector('.menu-introduction').value;
                const menuPrice = menuItem.querySelector('.menu-price').value;
                const menuImageElement = menuItem.querySelector('.file-style');

                // 메뉴 이름, 가격이 비어있으면 경고 메시지 표시
                if (!menuName || !menuPrice) {
                    hasIncompleteMenuData = true;
                    resolve(); // 계속 진행하도록 resolve 호출
                    return;
                }

                // 이미지가 선택되지 않은 경우 경고 메시지 표시
                if (!menuImageElement.files[0]) {
                    alert('이미지를 선택하세요.');
                    hasIncompleteMenuData = true;
                    resolve(); // 계속 진행하도록 resolve 호출
                    return;
                }

                // 파일 리더 사용하여 이미지를 Base64로 인코딩
                const reader = new FileReader();
                reader.onload = function(event) {
                    const base64Image = event.target.result.split(',')[1]; // Base64 문자열만 추출
                    menus.push({
                        menuName: menuName,
                        menuIntroduction: menuIntroduction,
                        menuPrice: parseInt(menuPrice),
                        menuImage: base64Image
                    });
                    resolve(); // 메뉴 처리 완료 후 resolve 호출
                };
                reader.readAsDataURL(menuImageElement.files[0]);
            });
        });

        categoryPromises.push(Promise.all(menuPromises).then(() => {
            // 모든 메뉴가 처리된 후 카테고리 배열에 추가
            if (menus.length > 0) {
                categories.push({
                    menuCategoryName: menuCategoryName,
                    menus: menus
                });
            } else {
                hasEmptyMenu = true;
            }
        }));
    });

    Promise.all(categoryPromises).then(() => {
        finalizeValidation(categories);
    });
};

function finalizeValidation(categories) {
    if (hasIncompleteMenuData) {
        alert("필수 항목을 입력하세요.");
        return;
    }

    if (hasEmptyMenu || categories.length === 0) {
        alert("하나 이상의 카테고리 또는 메뉴를 입력하세요.");
        return;
    }

    // 모든 유효성 검사를 통과하면 추가 작업 수행
    console.log("모든 입력이 완료되었습니다.", categories);
    sendData(categories); // 데이터를 서버로 전송
}

function sendData(categories) {
    // 전체 데이터를 하나의 객체로 준비
    const data = {
        categories: categories
    };
    console.log("전송할 데이터:", JSON.stringify(data, null, 2));

    // 첫 번째 Ajax 요청 (jQuery 사용)
    $.ajax({
        url: '/market/menu/save',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(response) {
            alert('메뉴 저장 완료');
            console.log('Response data:', response);
            window.location.href = '/market/option';
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('저장 실패. 등록된 가게정보가 없습니다.');
            console.error('Error:', textStatus, errorThrown);
        }
    });
}



$(".input-file-button").on('change',function(){
  var fileName = $(".input-file-button").val();
  $(".file-style").val(fileName);
});

document.addEventListener('DOMContentLoaded', function() {
    const saveButton = document.getElementById('save-menu-button');
    if (saveButton) {
        saveButton.addEventListener('click', function(event) {
            event.preventDefault();
            window.saveMenu();
        });
    }

    document.addEventListener('click', function(e) {
        const target = e.target;

        // 메뉴 추가하기 버튼이 클릭된 경우
        if (target.classList.contains('add-menu-button')) {
            menuCount++;
            const menuContainer = target.closest('.category-container').querySelector('.menu-container');

            const menuHtml = `
            <div class="menu-item" id="menu-${menuCount}">
                <ul class='no_dot'>
                    <li>
                    <button class="delete-menu-button del-button" data-menu-id="${menuCount}">메뉴 삭제</button>
                        <div class="line-container margin-top-20">
                            <div class="length-container">
                                <div class="s-info-text essential">메뉴를 입력하세요.</div>
                                <input type="text" id="marketMenu-${menuCount}" th:field="*{marketMenu}" name="marketMenu" class="market-menu m-input-box margin-top-10"/>
                            </div>
                            <div class="length-container margin-left-10">
                                <div class="s-info-text essential">가격</div>
                                <input type="number" id="menuPrice-${menuCount}" th:field="*{menuPrice}" name="menuPrice" value="0" class="menu-price s-input-box margin-top-10"/>
                            </div>
                        </div>
                        <div class="s-info-text margin-top-10">메뉴를 설명해주세요.</div>
                        <textarea id="menuIntro-${menuCount}" th:field="*{menuIntro}" name="menuIntro" class="menu-introduction ll-input-box margin-top-10"></textarea>
                        <div class="s-info-text margin-top-10 essential">메뉴 사진 등록</div>

                        <div class="line-container">
                            <img src="/images/no-image.jpg" class="img-style margin-top-15" id="img-preview-${menuCount}"/>
                            <label class="input-file-button" for="input-file-${menuCount}">이미지 업로드</label>
                            <input type="file" id="input-file-${menuCount}" th:field="*{menuImage}" name="menuImage" class="file-style" onchange="previewImage(event, ${menuCount})" style="display:none">
                        </div>
                        <hr class="hr-margin"/>
                    </li>
                </ul>
            </div>
            `;

            menuContainer.insertAdjacentHTML('beforeend', menuHtml);
        }
        // 메뉴 카테고리 추가하기 버튼이 클릭된 경우
        else if (target.id === 'add-category-button') {
            categoryCount++;
            const categoryContainer = document.getElementById('category-container');

            const categoryHtml = `
                <div class="category-container" id="category-${categoryCount}">
                    <ul class='no_dot'>
                        <li>
                            <button class="delete-category-button del-button" data-category-id="${categoryCount}">카테고리 삭제</button>
                            <div class="length-container margin-top-20">
                                <div class="info-text">메뉴 카테고리를 입력하세요.</div>
                                <input type="text" id="menuCategory-${categoryCount}" th:field="*{menuCategory}" name="menuCategory" class="menu-category l-input-box margin-top-10"/>
                            </div>
                            <div class="menu-container">
                                <!-- 여기에 해당 카테고리의 메뉴들이 추가될 부분 -->
                                <hr class="hr-margin"/>
                            </div>
                            <div class="s-line-text add-menu-button">메뉴 추가하기 +</div>
                        </li>
                    </ul>
                    <hr class="hr-margin-b"/>
                </div>
            `;

            categoryContainer.insertAdjacentHTML('beforeend', categoryHtml);
        }

        // 카테고리 삭제하기 버튼이 클릭된 경우
        else if (target.classList.contains('delete-category-button')) {
            const categoryId = target.getAttribute('data-category-id');
            deleteCategory(categoryId);
        }

        // 메뉴 삭제하기 버튼이 클릭된 경우
        else if (target.classList.contains('delete-menu-button')) {
            const menuId = target.getAttribute('data-menu-id');
            deleteMenu(menuId);
        }
    });

    function deleteMenu(menuId) {
        const menuElement = document.getElementById(`menu-${menuId}`);
        if (!confirm("아래 메뉴를 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            menuElement.remove();
        }
    }

    function deleteCategory(categoryId) {
        const categoryElement = document.getElementById(`category-${categoryId}`);
        if (!confirm("카테고리를 삭제하면 하위 메뉴가 삭제됩니다. 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            categoryElement.remove();
        }
    }
});

// 이미지 미리보기 함수
function previewImage(event, count) {
    const reader = new FileReader();
    reader.onload = function() {
        const output = document.getElementById(`img-preview-${count}`);
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}