$('#profile-user-image').click(function(e){
    $('#profile-user-img-save-btn').click();
});

$('#profile-user-img-save-btn').change(function (e) {
    let photo_path = $('#profile-user-image').attr('src');
    let my_photo; //회원이 업로드할 이미지 담을 변수
    my_photo = this.files[0];
    if(!my_photo){
        $('#profile-user-image').attr('src', photo_path);
        return
    }
    //이미지 미리보기 처리
    let reader = new FileReader();
    reader.readAsDataURL(my_photo);

    reader.onload = function(){
        $('#profile-user-image').attr('src', reader.result);
    }
});