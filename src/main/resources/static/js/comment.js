const COMMENT = (function () {
    'use strict';

    const commentEditTemplate = `<form accept-charset="utf-8" method="post" action ="{{url}}">
        <div class="add-comment">
        <input id="{{contentsId}}" name="contents" type="hidden">
        <div id="{{editorId}}" class = "comment-editor"></div>
        <input name="_method" type="hidden" value="put"/>
        <button class="btn btn-default" id="{{buttonId}}" type="submit">댓글 수정
        </button></div>
        </form>`;
    const compiledCommentEditTemplate = Handlebars.compile(commentEditTemplate);
    const CommentController = function () {
        const commentService = new CommentService();

        const updateComment = function () {
            const comments = document.getElementById('comment-list');
            comments.addEventListener('click', commentService.update);
        };

        const deleteComment = function () {
            const comments = document.getElementById('comment-list');
            comments.addEventListener('click', commentService.remove);
        }
        const init = function () {
            updateComment();
            deleteComment();
        };
        return {
            init: init
        };
    };

    const CommentService = function () {
        const update = function (event) {
            const target = event.target;
            const parent = target.closest('li');
            const form = parent.getElementsByTagName('form')[0];
            if (
                target.classList.contains('comment-edit') &&
                parent.getElementsByClassName('comment-editor').length === 0
            ) {
                const url = form.action;
                const tokens = form.action.split('/');
                const commentId = tokens[tokens.length - 1];
                const editorId = 'editSection' + commentId;
                const contentsId = 'comment-contents' + commentId;
                const buttonId = 'comment-edit-finish-button' + commentId;

                parent.insertAdjacentHTML(
                    'beforeend',
                    compiledCommentEditTemplate({
                        url: url,
                        editorId: editorId,
                        contentsId: contentsId,
                        buttonId: buttonId
                    })
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

        return {
            update: update,
            remove: remove
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
