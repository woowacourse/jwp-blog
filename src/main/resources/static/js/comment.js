function editTextOn(commentId) {
    document.getElementById('comment-div-' + commentId).style.display = 'none';
    document.getElementById('edit-a-' + commentId).style.display = 'none';
    document.getElementById('delete-a-' + commentId).style.display = 'none';

    document.getElementById('edit-section-' + commentId).style.display = 'block';
    document.getElementById('update-a-' + commentId).style.display = 'block';
    document.getElementById('cancel-a-' + commentId).style.display = 'block';
}

function editTextOff(commentId) {
    document.getElementById('comment-div-' + commentId).style.display = 'block';
    document.getElementById('edit-a-' + commentId).style.display = 'block';
    document.getElementById('delete-a-' + commentId).style.display = 'block';

    document.getElementById('edit-section-' + commentId).style.display = 'none';
    document.getElementById('update-a-' + commentId).style.display = 'none';
    document.getElementById('cancel-a-' + commentId).style.display = 'none';
}

function deleteComment(articleId, commentId) {
    const deleteForm = document.createElement('form');
    deleteForm.setAttribute("method", "Post");
    deleteForm.setAttribute("action", "/article/" + articleId + "/comment/" + commentId);

    const hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "_method");
    hiddenField.setAttribute("value", "delete");
    deleteForm.appendChild(hiddenField);

    document.body.appendChild(deleteForm);
    deleteForm.submit();
}

function getCommentTemplate(comment, articleId) {
    return `<li class="comment-item border bottom mrg-btm-30">` +
        `<input type="hidden" value="${comment.id}">` +
        `<img alt="" class="thumb-img img-circle" src="https://avatars3.githubusercontent.com/u/50367798?v=4">` +
        `<div class="info">` +
        `<span class="text-bold inline-block" href=""> ${comment.commenter.name} </span>` +
        `<span class="sub-title inline-block pull-right">` +
        `<a id="edit-a-${comment.id}" onclick="editTextOn(${comment.id})">수정</a>` +
        `<a id="delete-a-${comment.id}" onclick="deleteComment(${articleId}, ${comment.id})">삭제</a>` +
        `<a id="update-a-${comment.id}" style="display: none">저장</a>` +
        `<a id="cancel-a-${comment.id}" onclick="editTextOff(${comment.id})" style="display: none">취소</a>` +
        `<span>${comment.date}</span>` +
        `</span>` +
        `<p id="comment-div-${comment.id}" class="width-80">${comment.contents}</p>` +
        `<textarea class="edit" id="edit-section-${comment.id}" name="edit-contents" style="display: none">${comment.contents}</textarea>` +
        `</div>` +
        `</li>`;
}

document.getElementById('save-btn').addEventListener('click', function () {
    const articleId = document.getElementById('articleId').value;
    const commentList = document.getElementById('comment-list');
    const contents = document.getElementById('comment-contents').value;
    const commentData = {contents: contents};

    fetch("/article/" + articleId + "/comment", {
        method: "POST",
        headers: {'Content-Type': 'application/json; charset=utf-8'},
        body: JSON.stringify(commentData)
    })
        .then(response => response.json())
        .then(json => {
            console.log(json);
            commentList.insertAdjacentHTML('beforeend', getCommentTemplate(json, articleId));
            editor.setMarkdown('');
            document.getElementById('comment-contents').value = '';
        });
});

document.getElementById('comment-list').addEventListener('click',
    function (event) {
        const articleId = document.getElementById('articleId').value;
        const commentId = event.target.parentNode.parentNode.parentNode.querySelector("input").value;
        const updateContents = document.getElementById('edit-section-' + commentId).value;
        const updateCommentData = {contents: updateContents};

        if (event.target.innerText === "저장") {
            fetch("/article/" + articleId + "/comment/" + commentId, {
                method: "PUT",
                headers: {'Content-Type': 'application/json; charset=utf-8'},
                body: JSON.stringify(updateCommentData)
            })
                .then(response => response.json())
                .then(json => {
                    console.log(json);
                    editTextOff(commentId);
                    document.getElementById('comment-div-' + commentId).innerText = json.contents;
                    document.getElementById('edit-section-' + commentId).value = json.contents;
                });
        }
    });