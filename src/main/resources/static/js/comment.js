let commentList = document.getElementById("comment-list");
commentList.addEventListener("click", function (ev) {
    if (event.target.classList.contains("target")) {
        event.target.parentNode.parentNode.classList.toggle('editing');
    }
});