function deleteKeyA() {
var key = document.getElementById("A").value;
    $.post('/delete', {key: key});
}
function deleteKeyB() {
var key = document.getElementById("B").value;
    $.post('/delete', {key: key});
}
function deleteKeyC() {
var key = document.getElementById("C").value;
    $.post('/delete', {key: key});
}
function deleteKeyD() {
var key = document.getElementById("D").value;
    $.post('/delete', {key: key});
}