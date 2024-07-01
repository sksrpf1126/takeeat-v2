//==== 리뷰 별점 ====
$('.star_rating > .star').click(function() {
    $(this).parent().children('span').removeClass('on');
    $(this).addClass('on').prevAll('span').addClass('on');
})

//==== 이미지 업로드====
document.getElementById('fileInput').addEventListener('change', function(event) {
    const files = event.target.files;
    const imagePreviewContainer = document.getElementById('imagePreviewContainer');

    imagePreviewContainer.innerHTML = '';

    //개수 제한
    if (files.length > 3) {
        alert('사진은 최대 3장까지 첨부할 수 있습니다.');
        event.target.value = '';
        return;
    }

    //확장자 제한
    var validExtensions = ['jpg', 'jpeg', 'png', 'gif'];
    var maxSize = 5 * 1024 * 1024; //10MB 사이즈 제한(개당)
    for(var i = 0; i < fileInput.files.length; i++) {
        var fileName = fileInput.files[i].name;
        var fileSize = fileInput.files[i].size;
        var fileExtension = fileName.split('.').pop().toLowerCase();

        if (!validExtensions.includes(fileExtension)) {
            alert("올바른 이미지 확장자 jpg, jpeg, png, gif 를 선택하세요.\n문제 파일: " + fileName);
            return;
        }

        if (fileSize > maxSize){
            alert("각 파일의 사이즈는 10MB 이내로 가능합니다.\n문제 파일: " + fileName);
            return;
        }
    }

    //업로드 이미지 미리보기
    Array.from(files).forEach(file => {
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.classList.add('preview-image');
                imagePreviewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        }
    });
});

document.getElementById('deleteImageBtn').addEventListener('click', function () {
    const fileInput = document.getElementById('fileInput');
    fileInput.value = ''; // 파일 입력 필드 초기화

    const imagePreviewContainer = document.getElementById('imagePreviewContainer');
    imagePreviewContainer.innerHTML = ''; // 미리보기 초기화
});
