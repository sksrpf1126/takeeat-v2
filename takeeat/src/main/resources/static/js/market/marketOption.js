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
    let selectedMenuValues = []; // 선택된 옵션을 추적하는 전역 배열

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
                                    <input type="text" id="marketOption-${optionCount}" th:field="*{optionName}" name="marketOption" class="m-input-box margin-top-10"/>
                                </div>
                                <div class="length-container margin-left-10">
                                    <div class="s-info-text">가격</div>
                                    <input type="number" id="optionPrice-${optionCount}" th:field="*{optionPrice}" name="optionPrice" class="s-input-box margin-top-10"/>
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
                                        <input type="text" id="optionCategory-${categoryCount}" th:field="*{optionCategory}" name="optionCategory" class="l-input-box-option margin-top-10"/>
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
                                            <input type="radio" id="single-${categoryCount}" th:field="*{optionSelect}" th:value="'single'" name="select-${categoryCount}" onclick="handleOptionSelect(${categoryCount}, 'single')" checked/>단일
                                            <input type="radio" id="multi-${categoryCount}" th:field="*{optionSelect}" th:value="'multi'" name="select-${categoryCount}" style="margin-left:11px;" onclick="handleOptionSelect(${categoryCount}, 'multi')"/>다중
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
                                    <input type="number" id="maxCount-${categoryCount}" th:field="*{optionMaxCount}" name="maxCount" class="s-input-box margin-top-10" value="1" disabled/>
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

    function loadMenuData(categoryCount) {
        fetch('/market/menus') // 혹은 $.ajax 사용
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
                updateMenuDropdown(data, categoryCount); // 메뉴 드롭다운 업데이트
            })
            .catch(error => {
                console.error('Error fetching menu data:', error);
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
            updateSelectedMenuValues(); // 선택된 옵션 값을 업데이트
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
            updateSelectedMenuValues(); // 선택된 옵션 값을 업데이트
        }
    }
});

function saveOption() {
    let categories = [];
    let hasIncompleteOptionData = false; // 필수 항목 누락 여부 확인 변수
    let hasEmptyOption = false; // 빈 옵션 여부 확인 변수
    let hasNoCategories = true; // 카테고리 입력 여부 확인 변수

    // 모든 카테고리와 옵션 정보 수집
    document.querySelectorAll('.category-container').forEach(categoryContainer => {
        let options = [];
        const optionCategoryName = categoryContainer.querySelector('.option-category').value;
        const optionMaxCount = categoryContainer.querySelector('.option-max-count').value;
        const optionSelect = categoryContainer.querySelector('.option-select').value;

        if (optionCategoryName) {
            hasNoCategories = false; // 카테고리가 입력되었음을 표시
        }

        // 현재 카테고리의 옵션 수집
        categoryContainer.querySelectorAll('.option-item').forEach(optionItem => {
            const optionName = optionItem.querySelector('.m-input-box').value;
            const optionPrice = optionItem.querySelector('.s-input-box').value;

            // 옵션 데이터가 모두 올바르게 수집되었는지 확인
            if (optionName && optionPrice) {
                let optionObject = {
                    optionName: optionName,
                    optionPrice: parseInt(optionPrice)
                };
                options.push(optionObject);
            } else {
                console.warn("옵션 데이터가 완전하지 않습니다:", optionItem);
                hasIncompleteOptionData = true;
            }
        });

        // 현재 카테고리 정보를 categories 배열에 추가
        if (optionCategoryName && options.length > 0) {
            categories.push({
                optionCategoryName: optionCategoryName,
                optionMaxCount: optionMaxCount,
                optionSelect: optionSelect,
                options: options
            });
        } else if ((optionCategoryName && options.length === 0) || optionCategoryName === null) {
            console.warn("옵션이 없습니다:", categoryContainer);
            hasEmptyOption = true;
        } else {
            console.warn("카테고리가 없습니다:", categoryContainer);
            hasNoCategories = true;
        }
    });

    if (hasIncompleteOptionData) {
        alert("필수항목을 입력하세요.");
        return;
    }

    // 경고 메시지 표시
    if (hasEmptyOption) {
        alert("옵션을 입력하세요.");
        return;
    }

    if (categories.length === 0) {
        alert("하나 이상의 카테고리 또는 옵션을 입력하세요.");
        return;
    }

    if (hasNoCategories) {
        alert("카테고리를 입력하세요.")
        return;
    }

    // 전체 데이터를 하나의 객체로 준비
    const data = {
        categories: categories
    };

    // 데이터 구조와 JSON 문자열 확인 (디버깅용)
    console.log("전송할 데이터:", JSON.stringify(data, null, 2));

   // AJAX 요청
       fetch('/market/option/save', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(data)
       })
       .then(response => {
           if (response.ok) {
               return response.json();
           }
           throw new Error('Network response was not ok.');
       })
       .then(data => {
           alert('옵션 저장 완료');
           console.log('Response data:', data);
           window.location.href = '/';
       })
       .catch(error => {
           alert('저장 실패');
           console.error('Error:', error);
       });
   };
