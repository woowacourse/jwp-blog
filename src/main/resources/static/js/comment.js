const COMMENT = (function () {
    'use strict';
    const CommentController = function () {
        const commentService = new CommentService();

        const updateCommentForm = function () {
            const comments = document.getElementById('comment-list');
            comments.addEventListener('click', commentService.updateForm);
        };

        const deleteComment = function () {
            const comments = document.getElementById('comment-list');
            comments.addEventListener('click', commentService.remove);
        }
        const updateComment = function () {
            const comments = document.getElementById('comment-list');
            comments.addEventListener('click', commentService.update);

        }
        const init = function () {
            updateCommentForm();
            deleteComment();
            updateComment();
        };
        return {
            init: init
        };
    };

    const CommentService = function () {
        const updateForm = function (event) {
            const target = event.target;
            const parent = target.closest('li');
            if (
                target.classList.contains('comment-edit') &&
                parent.getElementsByClassName('comment-editor').length === 0
            ) {
                const commentId = parent.dataset.commentId;
                const editorId = 'editSection' + commentId;
                const contentsId = 'comment-contents' + commentId;
                const buttonId = 'comment-edit-finish-button' + commentId;

                const commentEditTemplate =
                    `<div class="add-comment edit-comment-form">
                    <input id="${contentsId}" name="contents" type="hidden">
                    <div id="${editorId}" class = "comment-editor"></div>
                    <button class="btn btn-default comment-edit-finish-button" id="${buttonId}" type="button">댓글 수정
                    </button></div>
                    `;

                parent.insertAdjacentHTML(
                    'beforeend', commentEditTemplate
                );
                const editor2 = new tui.Editor({
                    el: document.querySelector('#' + editorId),
                    initialEditType: 'markdown',
                    previewStyle: 'horizontal',
                    events: {
                        change: function () {
                            document.getElementById(contentsId).setAttribute('value', editor2.getMarkdown());
                        }
                    },
                    height: '200px'
                });
            }
        };
        const remove = function (event) {
            const targetButton = event.target
            if (targetButton.classList.contains("comment-del-button")) {
                const comment = targetButton.closest('li')
                let id = comment.dataset.commentId
                deleteCommentFetch(articleId, id, comment);
            }
        }

        const update = function (event) {
            const targetButton = event.target
            if (targetButton.classList.contains("comment-edit-finish-button")) {
                const comment = targetButton.closest('li')
                let id = comment.dataset.commentId
                let p = comment.getElementsByTagName("p")[0]
                let editedContents = document.getElementById("comment-contents" + id).value;
                console.log(editedContents)
                updateCommentFetch(articleId, id, p, editedContents);
            }
        }


        return {
            updateForm: updateForm,
            remove: remove,
            update: update
        };
    };

    const init = function () {
        const commentController = new CommentController();
        commentController.init();
    };

    return {
        init: init
    };
})();

window.onload = function() {
    // commentLoad();
    COMMENT.init();
}

const saveButton = document.querySelector('#comment-save-button');
const articleId = document.querySelector('#article-id').value;
saveButton.addEventListener('click', addCommentFetch);

function commentLoad(){
    fetch('/articles/' + articleId + '/comments',{
        method:'get',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        credentials: 'include'
    })
    .then(function (response) {
        if (response.ok) {
            return response.json();
        }
        throw response;
    })
    .then((json) => {
        console.log(json);
        let commentListDto = {
            comments: json.data.comments,
        };
        addCommentList(articleId, commentDto);
    })
    .catch((response) =>
        response.json().then(json =>
            alert(json.errorMessage)
        )
    );
}
function addCommentList(){
    const commentContainer = document.getElementById('comment-list');
    const commentList = commentLoad();
}


function addCommentFetch(e) {
    let contents = document.querySelector('#comment-contents').value;
    fetch('/articles/' + articleId + '/comments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({contents: contents}),
        credentials: 'include'
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            }
            throw response;
        })
        .then((json) => {
            console.log(json);
            let commentDto = {
                contents: json.data.contents,
                userName: json.data.userName,
                id: json.data.id
            };
            addComment(articleId, commentDto);
        })
        .catch((response) =>
            response.json().then(json =>
                alert(json.errorMessage)
            )
        );
}

function deleteCommentFetch(articleId, commentId, targetComment) {
    fetch('/articles/' + articleId + '/comments/' + commentId,
        {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            credentials: 'include'
        })
        .then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response;
        })
        .then(json => {
            targetComment.remove();
        })
        .catch(
            response => response.json().then((json) => alert(json.errorMessage))
        )
}

function updateCommentFetch(articleId, commentId, editParagraph, contents) {
    fetch('/articles/' + articleId + '/comments/' + commentId,
        {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({contents: contents}),
            credentials: 'include'
        })
        .then(response => {
            if (response.ok) {
                return response.json()
            }
            throw response;
        })
        .then(json => {
            editParagraph.innerText = contents;
            document.querySelectorAll('.edit-comment-form').forEach(e => e.parentNode.removeChild(e));
        })
        .catch(
            response => response.json().then((json) => alert(json.errorMessage))
        )
}



function addComment(articleId, commentDto) {
    console.log("abcd");
    let buttonTemplate = `
            <button class="btn btn-icon float-right pointer comment-del-button"
                    type="button">
                <i class="ti-trash text-dark font-size-16 pdd-horizon-5 comment-del-button"></i>
            </button>
        
        <button class="comment-edit float-right pointer btn btn-icon">
            <i class="comment-edit ti-pencil text-dark font-size-16 pdd-horizontal-5"></i>
        </button>`;

    let commentTemplate = `
        <li class="comment-item border bottom mrg-btm-30" data-comment-id ="${commentDto.id}">
            <img alt=""
                 class="thumb-img img-circle"
                 src="https://avatars2.githubusercontent.com/u/3433096?v=4">
            <div class="info">
                <span class="text-bold inline-block" >${commentDto.userName}</span>

                <span class="sub-title inline-block pull-right">
        <i class="ti-timer pdd-right-5"></i>p
        <span>6 min ago</span>
    </span>
                ${buttonTemplate}
                <p class="width-80">${commentDto.contents}</p>
            </div>
        </li>`;
    const comments = document.querySelector('#comment-list');
    comments.insertAdjacentHTML('beforeend', commentTemplate);
}
