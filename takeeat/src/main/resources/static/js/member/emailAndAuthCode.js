const emailInput = document.querySelector('#email');

$("#auth-code-btn").click(function() {

    $.ajax({
        url : '/member/email-send',
        type : 'POST',
        data : {email : emailInput.value},
        dataType : 'text',
        success : function(data){
            alert(data);
        },
        error: function (xhr, status, error) {
            const responseText = JSON.parse(xhr.responseText);
            alert(responseText.message);
        }
    });
});

$("#auth-code-btn-find-id").click(function() {

    $.ajax({
        url : '/member/email-send/find-id',
        type : 'POST',
        data : {email : emailInput.value},
        dataType : 'text',
        success : function(data){
            alert(data);
        },
        error: function(xhr, status, error){
            const responseText = JSON.parse(xhr.responseText);
            alert(responseText.message);
        }
    });
});