 document.addEventListener('DOMContentLoaded', function() {
    let optionCount = 0;
    let categoryCount = 0;

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
                                    <select id="menuNameDropdown" class="margin-bottom-15">
                                        <option value="">메뉴 선택</option>
                                        <!-- 서버에서 가져온 데이터가 이곳에 추가될 예정 -->
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

    // 서버에서 메뉴 이름 데이터를 가져와서 옵션으로 추가하는 함수
        function fetchMenuNamesAndPopulateDropdown() {
            fetch('/menuNames') // 위에서 정의한 API 엔드포인트
                .then(response => response.json())
                .then(data => {
                    const menuDropdown = document.getElementById('menuNameDropdown');
                    menuDropdown.innerHTML = ''; // 기존 옵션들 초기화

                    data.forEach(menu => {
                        const option = document.createElement('option');
                        option.value = menu.menuName;
                        option.textContent = menu.menuName;
                        menuDropdown.appendChild(option);
                    });
                })
                .catch(error => console.error('메뉴 이름을 불러오는 중 에러 발생:', error));
        }

        // 페이지 로드 시 메뉴 이름 드롭다운 초기화
        fetchMenuNamesAndPopulateDropdown();

        // 옵션 추가 버튼 처리 등 기존 코드 추가 및 수정

        // 선택된 메뉴에 따라 옵션을 추가하는 함수
        function addOptionsForMenu(menuName) {
            // 해당 메뉴에 대한 옵션을 추가하는 로직을 구현
            // 이 함수는 필요한 데이터를 서버에서 가져와서 처리해야 합니다.
        }

        // 메뉴 이름 선택 시 처리
        document.getElementById('menuNameDropdown').addEventListener('change', function() {
            const selectedMenuName = this.value;
            if (selectedMenuName) {
                addOptionsForMenu(selectedMenuName);
            }
        });

    function deleteOption(optionId) {
        // 옵션 삭제 처리 함수
        const optionElement = document.getElementById(`option-${optionId}`);
        if (!confirm("아래 옵션을 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            optionElement.remove();
        }
    }

    function deleteCategory(categoryId) {
        // 카테고리 삭제 처리 함수
        const categoryElement = document.getElementById(`category-${categoryId}`);
        if (!confirm("카테고리를 삭제하면 하위 옵션이 삭제됩니다. 정말 삭제하시겠습니까?")) {
            event.preventDefault();
        } else {
            categoryElement.remove();
        }
    }

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

    function toggleDropdown(categoryCount) {
        // 드롭다운 토글 처리 함수
        const dropdownContent = document.getElementById(`dropdownContent-${categoryCount}`);
        dropdownContent.classList.toggle('show');
    }

    function selectCategory(categoryCount, menuName) {
        // 메뉴 항목 선택 처리 함수
        const dropdownButton = document.getElementById(`dropdownButton-${categoryCount}`);
        dropdownButton.querySelector('.dropdown-text').textContent = menuName;
        const dropdownContent = document.getElementById(`dropdownContent-${categoryCount}`);
        dropdownContent.classList.remove('show');
    }

    window.addEventListener('click', function(event) {
        if (!event.target.matches('.dropdown-button') && !event.target.closest('.dropdown-content')) {
            const dropdowns = document.querySelectorAll('.dropdown-content');
            dropdowns.forEach(function(dropdown) {
                if (dropdown.classList.contains('show')) {
                    dropdown.classList.remove('show');
                }
            });
        }
    });


});