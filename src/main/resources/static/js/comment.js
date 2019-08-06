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
        "<form class='pull-right' method='post' style='display: inline-block;' action='/comments/" + json.id + "'>" +
        "<input name='_method' type='hidden' value='delete'>" +
        "<button class='btn-sm btn-link pull-right' style='border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem; padding-bottom: 0; padding-top: 0' " +
        " onclick='deleteComment(" + json.id + ")' type='button'>" +
        "<i class='ti-trash' style='color: gray;'/>" +
        "</button>" +
        "</form>" +
        "<form class='pull-right' method='post' style='display: inline-block;' action='/comments/" + json.id + "/edit'>" +
        "<button class='btn-sm btn-link pull-right' style='border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem; padding-bottom: 0; padding-top: 0' type='submit'>" +
        "<i class='ti-pencil' style='color: gray;'/>" +
        "</button>" +
        "</form>" +
        "<span class='sub-title inline-block pull-right'>" +
        "<i class='ti-timer pdd-right-5'/>" +
        "<span>" + json.updatedTime + "</span>" +
        "</span>" +
        "<p class='width-80' name='contents'>" + json.contents + "</p>" +
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