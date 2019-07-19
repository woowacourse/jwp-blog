const validate = (e) => {
    e.preventDefault();
    const first = document.getElementById("first-password");
    const second = document.getElementById("second-password");
    if (first.value !== second.value) {
        first.value = "";
        second.value = "";
        alert("Are you Fucking me?");
        return false;
    }
    document.forms[0].submit();
    return true;
}