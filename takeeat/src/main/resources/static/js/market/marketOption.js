let optionCount = 0; // 초기 옵션 카운트
let categoryCount = 0; // 초기 카테고리 카운트

document.addEventListener('click', function(e) {
    const target = e.target;

    // 옵션 추가하기 버튼이 클릭된 경우
    if (target.classList.contains('add-option-button')) {
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
                        <div class="dropdown">
                            <div class="dropdown-button margin-bottom-15" id="dropdownButton-${categoryCount}" onclick="toggleDropdown(${categoryCount})">
                                <p class="dropdown-text">메뉴를 선택하세요.</p>
                                <div class="dropdown-triangle"></div>
                            </div>
                            <div class="dropdown-content" id="dropdownContent-${categoryCount}">
                                <th:block th:each="menu : ${menuResponses}">
                                    <p onclick="selectCategory(${categoryCount}, [[${menu.menuName}]])">
                                        <span th:text="${menu.menuName}"></span>
                                    </p>
                                </th:block>
                            </div>
                        </div>
                        <div class="length-container margin-top-20">
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
    }

    // 카테고리 삭제하기 버튼이 클릭된 경우
    else if (target.classList.contains('delete-category-button')) {
        const categoryId = target.getAttribute('data-category-id');
        deleteCategory(categoryId);
    }

    // 옵션 삭제하기 버튼이 클릭된 경우
    else if (target.classList.contains('delete-option-button')) {
        const optionId = target.getAttribute('data-option-id');
        deleteOption(optionId);
    }
});

function deleteOption(optionId) {
    const optionElement = document.getElementById(`option-${optionId}`);
    if (!confirm("아래 옵션을 정말 삭제하시겠습니까?")) {
        event.preventDefault();
    } else {
        optionElement.remove();
    }
}

function deleteCategory(categoryId) {
    const categoryElement = document.getElementById(`category-${categoryId}`);
    if (!confirm("카테고리를 삭제하면 하위 옵션이 삭제됩니다. 정말 삭제하시겠습니까?")) {
        event.preventDefault();
    } else {
        categoryElement.remove();
    }
}

function handleOptionSelect(categoryCount, optionType) {
    console.log(`Clicked: ${optionType}`);
    var maxCountInput = document.getElementById(`maxCount-${categoryCount}`);

    if (optionType === 'single') {
        maxCountInput.value = 1;
        maxCountInput.disabled = true;
    } else if (optionType === 'multi') {
        maxCountInput.disabled = false;
    }
}

function toggleDropdown(categoryId) {
    var dropdownContent = document.getElementById(`dropdownContent-${categoryId}`);
    dropdownContent.classList.toggle('show');
}

function selectCategory(categoryId, category) {
    document.getElementById(`dropdownButton-${categoryId}`).getElementsByClassName('dropdown-text')[0].innerText = category;
    document.getElementById(`dropdownContent-${categoryId}`).classList.remove('show');
}

document.addEventListener('click', function(event) {
    var dropdowns = document.getElementsByClassName('dropdown-content');
    for (var i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i];
        if (!openDropdown.contains(event.target) && openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
        }
    }
});