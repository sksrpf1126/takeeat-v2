function handleOptionSelect(categoryCount, optionType) {
    // 옵션 선택 방법 처리 함수
    console.log(`Clicked: ${optionType}`);
    const maxCountInput = document.getElementById(`maxCount-${categoryCount}`);

    if (optionType === 'single') {
        maxCountInput.value = 1;
        maxCountInput.disabled = true;
    } else if (optionType === 'multi') {
        maxCountInput.disabled = false;
    }
}

document.addEventListener('DOMContentLoaded', function() {
    let optionCount = 0;
    let categoryCount = 0;
    let selectedMenuValues = []; // 선택된 메뉴를 추적하는 전역 배열

    document.addEventListener('click', function(event) {
        const target = event.target;

        if (target.classList.contains('add-option-button')) {
            // 옵션 추가 버튼 처리
            optionCount++;
            const optionContainer = target.closest('.category-container').querySelector('.option-container');

            const optionHtml = `
                <div id="option-${optionCount}">
                    <ul class='no_dot'>
                        <li>
                            <button class="delete-option-button del-button" data-option-id="${optionCount}">옵션 삭제</button>
                            <div class="line-container margin-top-20">
                                <div class="length-container">
                                    <div class="s-info-text">옵션을 입력하세요.</div>
                                    <input type="text" id="marketOption-${optionCount}" name="marketOption" class="m-input-box margin-top-10"/>
                                </div>
                                <div class="length-container margin-left-10">
                                    <div class="s-info-text">가격</div>
                                    <input type="number" id="optionPrice-${optionCount}" name="optionPrice" class="s-input-box margin-top-10"/>
                                </div>
                            </div>
                            <hr class="hr-margin"/>
                        </li>
                    </ul>
                </div>
            `;

            optionContainer.insertAdjacentHTML('beforeend', optionHtml);
        } else if (target.id === 'add-category-button') {
            // 카테고리 추가 버튼 처리
            categoryCount++;
            const categoryContainer = document.getElementById('category-container');

            const categoryHtml = `
                <div class="category-container" id="category-${categoryCount}">
                    <ul class='no_dot'>
                        <li>
                            <button class="delete-category-button del-button" data-category-id="${categoryCount}">카테고리 삭제</button>

                            <div class="length-container margin-top-20">
                                <select id="menuNameDropdown-${categoryCount}" class="dropdown margin-bottom-15">
                                    <option value="" selected disabled hidden>메뉴를 선택하세요.</option>
                                </select>
                                <div class="line-container">
                                    <div class="length-container">
                                        <div class="info-text">옵션 카테고리를 입력하세요.</div>
                                        <input type="text" id="optionCategory-${categoryCount}" name="optionCategory" class="l-input-box-option margin-top-10"/>
                                    </div>
                                    <div class="length-container select-option">
                                        <div class="line-container">
                                            <div class="info-text">옵션선택방법</div>
                                            <div class="tip-container">
                                                <div class="tip">
                                                    <p>옵션의 선택 방법을 알려주세요.<br>옵션을 여러 개 선택 할 수 있으면 다중선택입니다.</p>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="line-container margin-top-10">
                                            <input type="radio" id="single-${categoryCount}" name="select-${categoryCount}" onclick="handleOptionSelect(${categoryCount}, 'single')" checked/>단일
                                            <input type="radio" id="multi-${categoryCount}" name="select-${categoryCount}" style="margin-left:11px;" onclick="handleOptionSelect(${categoryCount}, 'multi')"/>다중
                                        </div>
                                    </div>
                                </div>
                                <div class="length-container margin-top-10">
                                    <div class="line-container">
                                        <div class="s-info-text">옵션최대수량</div>
                                        <div class="tip-container-center">
                                            <div class="tip">
                                                <p>선택 가능한 최대 수량을 제시해주세요.<br>단일선택은 1개만 가능합니다.</p>
                                            </div>
                                        </div>
                                    </div>
                                    <input type="number" id="maxCount-${categoryCount}" name="maxCount" class="s-input-box margin-top-10" value="1" disabled/>
                                </div>
                            </div>
                            <div class="option-container">
                                <!-- 여기에 해당 카테고리의 메뉴들이 추가될 부분 -->
                                <hr class="hr-margin"/>
                            </div>
                            <div class="s-line-text add-option-button">옵션 추가하기 +</div>
                        </li>
                    </ul>
                    <hr class="hr-margin-b"/>
                </div>
            `;

            categoryContainer.insertAdjacentHTML('beforeend', categoryHtml);

            // 드롭다운 메뉴에서 선택 변경 시 이벤트 처리
            document.getElementById(`menuNameDropdown-${categoryCount}`).addEventListener('change', function() {
                updateSelectedMenuValues();
            });

            // 새로 추가된 카테고리에 메뉴 데이터 로드
            loadMenuData(categoryCount);
        } else if (target.classList.contains('delete-category-button')) {
            // 카테고리 삭제 버튼 처리
            const categoryId = target.getAttribute('data-category-id');
            deleteCategory(categoryId);
        } else if (target.classList.contains('delete-option-button')) {
            // 옵션 삭제 버튼 처리
            const optionId = target.getAttribute('data-option-id');
            deleteOption(optionId);
        }
    });

    // 초기 데이터 로드 함수
    function loadMenuData(categoryCount) {
        $.ajax({
            url: '/market/menus', // 서버의 엔드포인트
            method: 'GET',
            success: function(data) {
                updateMenuDropdown(data, categoryCount); // 데이터를 받아서 업데이트
            },
            error: function(xhr, status, error) {
                console.error('Error fetching menu data:', error);
            }
        });
    }

    // 드롭다운 메뉴 업데이트 함수
    function updateMenuDropdown(menuResponses, categoryCount) {
        const selectElement = $(`#category-${categoryCount} select.dropdown`); // 해당 카테고리의 드롭다운 선택
        const currentSelected = selectElement.val(); // 현재 선택된 값 저장
        selectElement.empty(); // 기존 옵션 모두 제거
        selectElement.append('<option value="" selected disabled hidden>메뉴를 선택하세요.</option>'); // 기본 옵션 추가

        // 메뉴 옵션 추가
        menuResponses.forEach(function(menu) {
            // 이미 선택된 메뉴가 아닌 경우에만 추가
            if (!selectedMenuValues.includes(menu.menuName) || menu.menuName === currentSelected) {
                const option = $('<option>').val(menu.menuName).text(menu.menuName);
                if (menu.menuName === currentSelected) {
                    option.attr('selected', 'selected'); // 현재 선택된 옵션 유지
                }
                selectElement.append(option);
            }
        });
        // 선택된 메뉴가 있으면 해당 드롭다운 비활성화
        if (currentSelected) {
            selectElement.prop('disabled', true);
        }
    }

    // 선택된 메뉴 값을 업데이트하는 함수
    function updateSelectedMenuValues() {
        selectedMenuValues = []; // 배열 초기화

        // 모든 드롭다운에서 선택된 값을 추출하여 배열에 저장
        $('select.dropdown').each(function() {
            const selectedOption = $(this).val();
            if (selectedOption) {
                selectedMenuValues.push(selectedOption);
            }
        });

        // 각 드롭다운을 다시 로드하여 선택된 값을 제외하고 옵션을 업데이트
        $('select.dropdown').each(function() {
            const categoryId = $(this).attr('id').split('-')[1];
            loadMenuData(categoryId);
        });
    }

    function deleteOption(optionId) {
        // 옵션 삭제 처리 함수
        const optionElement = document.getElementById(`option-${optionId}`);
        const selectElement = $(optionElement).closest('.category-container').find('select.dropdown');
        const selectedOption = selectElement.val();

        if (selectedOption) {
            // 선택된 옵션을 selectedMenuValues에서 제거
            const index = selectedMenuValues.indexOf(selectedOption);
            if (index !== -1) {
                selectedMenuValues.splice(index, 1);
            }
        }

        if (!confirm("아래 옵션을 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            optionElement.remove();
            updateSelectedMenuValues(); // 선택된 메뉴 값을 업데이트
        }
    }

    function deleteCategory(categoryId) {
        // 카테고리 삭제 처리 함수
        const categoryElement = document.getElementById(`category-${categoryId}`);
        const selectElement = $(categoryElement).find('select.dropdown');
        const selectedOption = selectElement.val();

        if (selectedOption) {
            // 선택된 옵션을 selectedMenuValues에서 제거
            const index = selectedMenuValues.indexOf(selectedOption);
            if (index !== -1) {
                selectedMenuValues.splice(index, 1);
            }
        }

        if (!confirm("카테고리를 삭제하면 하위 옵션이 삭제됩니다. 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            categoryElement.remove();
            updateSelectedMenuValues(); // 선택된 메뉴 값을 업데이트
        }
    }
});