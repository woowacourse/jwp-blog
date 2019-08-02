const validate = (e) => {
    e.preventDefault();
    const first = document.getElementById("first-password");
    const second = document.getElementById("second-password");
    if (first.value !== second.value) {
        first.value = "";
        second.value = "";
        alert("비밀번호가 다릅니다");
        return false;
    }
    document.forms[0].submit();
    return true;
}