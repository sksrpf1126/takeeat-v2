//=== marketStatus 값에 따라 checkbox에 checked 속성 부여 ===
var checked = false;
if (marketStatus == 'OPEN') {
    checked = true;
}

document.addEventListener('DOMContentLoaded', function() {
    var checkbox = document.getElementById('switch');
    checkbox.checked = checked;
});