let commentList = document.getElementById("comment-list");

fetch('/api/articles/' + articleId +  '/comments', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
}).then(function(response) {
    return response.json();
}).then(function(json) {
    removeAllCommentsFromCommentList(commentList);
    addCommentsToCommentList(json, commentList);
})

const commentTemplate =
    "<li class=\"comment-item border bottom mrg-btm-30\">\n"+
    "   <img class=\"thumb-img img-circle\" src=\"https://avatars3.githubusercontent.com/u/50367798?v=4\" alt=\"\">\n"+
    "   <span href=\"\" class=\"text-bold inline-block\">{{commenter}}</span>\n"+
    "   <span class=\"sub-title inline-block pull-right\">\n" +
    "      <i class=\"ti-timer pdd-right-5\"></i>\n" +
    "      <span>{{minAgo}}</span>\n" +
    "   </span>\n"+
    "   <div class=\"view info\">\n" +
    "      <p class=\"width-80 target\">{{comment}}</p>\n" +
    "      <button data-comment-id=\"{{id}}\" data-which-button=\"delete\" type=\"submit\" class=\"btn btn-default\">\n" +
    "         <i class=\"ti-close\"></i>\n" +
    "      </button>\n" +
    "   </div>\n" +
    "   <div class=\"edit info\">\n" +
    "      <input name=\"comment\" type=\"text\" value=\"{{comment}}\">\n" +
    "      <button data-comment-id=\"{{id}}\" data-which-button=\"update\" type=\"submit\" class=\"btn btn-default\">\n" +
    "         <i class=\"ti-save\"></i>\n" +
    "      </button>\n" +
    "   </div>\n" +
    "</li>";
const compiledCommentTemplate = Handlebars.compile(commentTemplate);

let saveBtn = document.getElementById("comment-save-btn");

saveBtn.addEventListener('click', function() {
    let requestDto = {
        comment: document.getElementById("article-contents").getAttribute("value")
    }
    fetch('/api/articles/' + articleId + '/comments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(requestDto)
    }).then(function(response) {
        return response.json();
    }).then(function(json) {
        removeAllCommentsFromCommentList(commentList);
        addCommentsToCommentList(json, commentList);
    })
});

commentList.addEventListener("click", function (ev) {
    if (event.target.classList.contains("target")) {
        event.target.parentNode.parentNode.classList.toggle('editing');
    }

    if (event.target.classList.contains("btn") && event.target.dataset.whichButton === 'delete') {
        fetch('/api/comments/' + event.target.dataset.commentId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        }).then(function(response) {
            return response.json();
        }).then(function(json) {
            removeAllCommentsFromCommentList(commentList);
            addCommentsToCommentList(json, commentList);
        })
    }

    if (event.target.classList.contains("btn") && event.target.dataset.whichButton === 'update') {
        let requestDto = {
            comment: event.target.parentNode.children[0].value
        }

        fetch('/api/comments/' + event.target.dataset.commentId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
            body: JSON.stringify(requestDto)
        }).then(function(response) {
            return response.json();
        }).then(function(json) {
            removeAllCommentsFromCommentList(commentList);
            addCommentsToCommentList(json, commentList);
        })
    }
});

function addCommentsToCommentList(json, comments) {
    for (var i = 0; i < json.length; i++) {
        var obj = json[i];
        comments.insertAdjacentHTML('beforeend', compiledCommentTemplate({
            "commenter": obj.commenter,
            "comment": obj.comment,
            "id": obj.id,
            "minAgo": obj.minAgo
        }));
    }
}

function removeAllCommentsFromCommentList(comments) {
    var child = comments.lastElementChild;
    while (child) {
        comments.removeChild(child);
        child = comments.lastElementChild;
    }
}