function createComment() {
    let data = {
        articleId: document.getElementById('articleId').value,
        contents: document.getElementById('commentContents').value
    };

    fetch('/comments', new postBodyData(data))
        .then(response => response.json())
.then(function (json) {
        // const li = document.createElement("li");
        // li.innerHTML = commentForm(json);
        $("#commentList").append(commentForm(json))
    });
}

function postBodyData(data) {
    this.method = "POST",
    this.headers = {
            "Content-Type": "application/json;charset=UTF-8"
    },
    this.body = JSON.stringify(data);
}

const commentForm = function (json) {
    return "<li id='comment_li_" + json.id + "' class='comment-item border bottom mrg-btm-30'>" +
        "<img alt='' class='thumb-img img-circle' src='https://avatars3.githubusercontent.com/u/50367798?v=4'>" +
        "<div class='info'>" +
        "<span class='text-bold inline-block' name='author'>" + json.userName + "</span>" +
        "<button class='btn-sm btn-link pull-right' style='border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem; padding-bottom: 0; padding-top: 0' " +
        " onclick='deleteComment(" + json.id + ")' type='button'>" +
        "<i class='ti-trash' style='color: gray;'/>" +
        "</button>" +
        "<button class='btn-sm btn-link pull-right' style='border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem; padding-bottom: 0; padding-top: 0' " +
        " onclick='updateCommentForm(" + json.id + ")' type='button'>" +
        "<i class='ti-pencil' style='color: gray;'/>" +
        "</button>" +
        "<span class='sub-title inline-block pull-right'>" +
        "<i class='ti-timer pdd-right-5'/>" +
        "<span>" + json.updatedTime + "</span>" +
        "</span>" +
        "<p class='width-80' id='edit_contents_" + json.id +"'>" + json.contents + "</p>" +
        "</div>" +
        "</li>"
};

function deleteComment(id) {
    fetch('/comments/' + id, new deleteBodyData())
        .then(response => response.json())
.then(function(json) {
        if (json.id == id) {
            document.getElementById("comment_li_" + id).remove();

        }
    });
}

function deleteBodyData() {
    this.method = "DELETE",
    this.headers = {
            "Content-Type": "application/json;charset=UTF-8"
    }
}

function updateCommentForm(id) {
    const editForm = document.createElement('span');
    editForm.innerHTML = '<textarea class="form-control" id="comment_li_edit" name="editText" rows="5"></textarea>' +
        '<button type="button" class="btn-sm btn-link pull-right" style="border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem; padding-bottom: 0; padding-top: 0" ' +
        'onclick="updateComment(' + id + ')">수정</button>';
    editForm.setAttribute("id", "comment_edit_form_" + id);
    document.getElementById("comment_li_" + id).appendChild(editForm);

    const commentContents = document.getElementById("edit_contents_" + id);
    document.getElementById("comment_li_edit").value = commentContents.innerHTML;
    commentContents.parentNode.removeChild(commentContents);
}

function updateComment(id) {
    let data = {
        articleId: id,
        contents: document.getElementById("comment_li_edit").value
    };

    fetch('/comments/' + id, new putBodyData(data))
        .then(response => response.json())
        .then(function (json) {
            const span = document.createElement('span');
            span.innerHTML = '<p id="edit_contents_' + id + '" class="width-80">' + json.contents + '</p>';
            let form = document.getElementById("comment_edit_form_" + id);
            form.parentNode.appendChild(span);
            form.remove();
    });
}

function putBodyData(data) {
    this.method = "PUT",
        this.headers = {
            "Content-Type": "application/json;charset=UTF-8"
        },
        this.body = JSON.stringify(data);
}