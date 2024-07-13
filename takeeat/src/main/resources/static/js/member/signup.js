let div;
let idCheckOk = false;
let idInput = document.querySelector('#member-login-id');

idInput.addEventListener('blur',idCheckBlur);  //focus 잃었을 때 이벤트
idInput.addEventListener('focus', idFocus);

function idFocus(){
    idCheckOk = false;
    if(document.querySelector('.check-word')){
        document.querySelector('#id-input-frame').removeChild(div);
    }
    if(document.querySelector('.check-word-ok')){
        document.querySelector('#id-input-frame').removeChild(div);
    }
}

function idCheckBlur(){  //focus 잃었을 때 실행되는 함수
    div = document.createElement('div');
    let asyncCheck = true;
    if(!/^[a-zA-Z0-9]{5,20}$/.test(idInput.value)){
        div.innerHTML ="<p>아이디는 숫자와 영문자 조합으로 5~ 20자리를 사용해야 합니다.</p>";
        idCheckOk = false;
    }else{
        let checkNumber = idInput.value.search(/[0-9]/g);
        let checkEnglish = idInput.value.search(/[a-z]/ig);

        if(checkNumber <0 || checkEnglish <0){
            div.innerHTML = "<p>숫자와 영문자를 혼용하여야 합니다.</p>";
            idCheckOk = false;
        }else{
            asyncCheck = false;
            $.ajax({
                url : '/member/id-check',
                type : 'GET',
                data : {memberLoginId : idInput.value},
                dataType : 'text',
                success : function(data){
                    idCheckOk = (data === "false") ? false : true;
                    if(idCheckOk){ //아이디 중복 아닐 시
                        div.innerHTML = "<p>사용가능한 아이디 입니다.</p>";
                        document.querySelector('#id-input-frame').appendChild(div).setAttribute('class','check-word-ok');
                    }else{
                        div.innerHTML = "<p>이미 존재하는 아이디입니다.</p>";
                        document.querySelector('#id-input-frame').appendChild(div).setAttribute('class','check-word');
                    }
                },
                error: function(){
                    alert('status 500(Server Error)');
                }
            }); //ajax 비동기
        } //else end
    } //else end
    if(asyncCheck){ //ajax 통신을 하지 않았을 시
        document.querySelector('#id-input-frame').appendChild(div).setAttribute('class','check-word');
    }
}


/**
 * 이메일 검증
 */
let emailCheckDiv;
let emailCheckOk = false;

emailInput.addEventListener('blur',emailCheckBlur);  //focus 잃었을 때 이벤트
emailInput.addEventListener('focus', emailFocus);

function emailFocus(){
    emailCheckOk = false;
    if(document.querySelector('.email-word')){
        document.querySelector('#email-input-frame').removeChild(emailCheckDiv);
    }
    if(document.querySelector('.email-word-ok')){
        document.querySelector('#email-input-frame').removeChild(emailCheckDiv);
    }
}

function emailCheckBlur(){  //focus 잃었을 때 실행되는 함수
    emailCheckDiv = document.createElement('div');
    $.ajax({
        url : '/member/email-check',
        type : 'GET',
        data : {email : emailInput.value},
        dataType : 'text',
        success : function(data){
            emailCheckOk = (data === "false") ? false : true;
            if(emailCheckOk){ //아이디 중복 아닐 시
                // emailCheckDiv.innerHTML = "<p>사용가능한 이메일 입니다.</p>";
                // document.querySelector('#email-input-frame').appendChild(emailCheckDiv);
                // emailCheckDiv.classList.add('check-word-ok', 'email-word-ok');
            }else{
                emailCheckDiv.innerHTML = "<p>사용할 수 없는 이메일 입니다.</p>";
                const parentDiv = document.querySelector('#email-input-frame');
                const authCodeInput = document.querySelector('#auth-code');
                parentDiv.insertBefore(emailCheckDiv, authCodeInput);
                emailCheckDiv.classList.add('check-word', 'email-word')
            }
        },
        error: function(){
            alert('status 500(Server Error)');
        }
    }); //ajax 비동기
}