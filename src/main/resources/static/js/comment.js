const commentList = document.getElementById("comment-list");
function editBtnClickHandler(event) {
    if (event.target.classList.contains("edit-comment-btn")){
        const editDiv = event.target.parentElement.parentElement.parentElement.lastElementChild;
        if (editDiv.style.visibility === "hidden"){
            editDiv.style.visibility = "visible";
        } else {
            editDiv.style.visibility = "hidden";
        }

    }
}

function cancleEditHandler(event) {
    if (event.target.classList.contains('comment-edit-text') && event.key === "Escape") {
        event.target.value = "";
        const editDiv = event.target.parentElement.parentElement;
        editDiv.style.visibility="hidden";
    }
}
commentList.addEventListener('click', editBtnClickHandler);
commentList.addEventListener('keyup', cancleEditHandler);