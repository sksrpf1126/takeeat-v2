$('#go-back').on('click', function() {
    window.history.back();
})

$('#go-login').on('click', function() {
    location.href = "/member/login";
})

$('#go-home').on('click', function() {
    location.href = "/";
})