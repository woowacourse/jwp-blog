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


                const template = commentEditTemplate(contentsId, editorId, buttonId)

                parent.insertAdjacentHTML(
                    'beforeend', template
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

COMMENT.init();

const saveButton = document.querySelector('#comment-save-button');
const articleId = document.querySelector('#article-id').value;
saveButton.addEventListener('click', addCommentFetch);

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
                contents: json['contents'],
                userName: json['userName'],
                id: json['id']
            };
            addComment(articleId, commentDto);
        })
        .catch(response => response.json().then((json) => alert(json.errorMessage))
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
    let buttonTemplate = commentButtonTemplate()

    let template = commentTemplate(commentDto, buttonTemplate)

    const comments = document.querySelector('#comment-list');
    comments.insertAdjacentHTML('beforeend', template);
}
