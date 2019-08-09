let commentList = document.getElementById("comment-list");
let saveBtn = document.getElementById("comment-save-btn");

commentList.addEventListener("click", function (event) {
    commentListEventHandler(event);
});

saveBtn.addEventListener('click', function () {
    commentPostRequest(commentList);
});

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

function commentListEventHandler(event) {
    if (event.target.classList.contains("target")) {
        event.target.parentNode.parentNode.classList.toggle('editing');
    }

    if (event.target.classList.contains("btn") && event.target.dataset.whichButton === 'delete') {
        fetch('/api/comments/' + event.target.dataset.commentId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            }
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
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
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            removeAllCommentsFromCommentList(commentList);
            addCommentsToCommentList(json, commentList);
        })
    }
}

function commentPostRequest(commentList) {
    let requestDto = {
        comment: document.getElementById("article-contents").getAttribute("value")
    }
    fetch('/api/articles/' + articleId + '/comments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(requestDto)
    }).then(function (response) {
        return response.json();
    }).then(function (json) {
        removeAllCommentsFromCommentList(commentList);
        addCommentsToCommentList(json, commentList);
    })
}

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