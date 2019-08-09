'use strict'

const editor = new tui.Editor({
    el: document.querySelector('#editSection'),
    initialEditType: 'markdown',
    previewStyle: 'horizontal',
    height: '200px',
});

const COMMENTAPP = (function () {
    const commentTemplate = function (comment) {
        return `<li class="comment-item border bottom mrg-btm-30" data-comment-id=${comment.commentId}>
        <img alt="" class="thumb-img img-circle" src="https://avatars3.githubusercontent.com/u/50367798?v=4">
        <div class="info">
        <span class="text-bold inline-block" name="author">${comment.userName}</span>
        <button class="btn-sm btn-link pull-right" type="button" 
            style="border-width: 0; text-decoration: none; padding-left: 1rem; padding-right: 0.5rem;
            padding-bottom: 0; padding-top: 0">
            <i class="ti-trash" style="color: gray;" data-btn-type="delete"></i>
        </button>
        <button class="btn-sm btn-link pull-right" type="button"
            style="border-width: 0; text-decoration: none;
            padding-left: 1rem; padding-right: 0.5rem;
            padding-bottom: 0; padding-top: 0">
            <i class="ti-pencil" style="color: gray;" data-btn-type="update"></i>
        </button>
        <span class="sub-title inline-block pull-right">
            <i class="ti-timer pdd-right-5"></i>
            <span>${comment.createdTime}</span>
        </span>
        <p class="width-80" name="contents">${comment.contents}</p>
        </div>
        </li>`
    }

    const commentAddBtn = document.getElementById('comment-add-btn');
    const commentUpdateBtn = document.getElementById('comment-update-btn');
    const updateCancelBtn = document.getElementById('update-cancel-btn');
    const commentList = document.getElementById('comment-list');

    let commentCount = {
        data: document.getElementById('comment-count'),
        up: function () {
            this.data.innerText = parseInt(this.data.innerText) + 1;
        },
        down: function () {
            this.data.innerText = parseInt(this.data.innerText) - 1;
        }
    }

    const CommentController = function () {
        const commentService = new CommentService()

        const addComment = function () {
            commentAddBtn.addEventListener('click', commentService.add);
        }

        const updateMode = function () {
            commentList.addEventListener('click', commentService.updateMode);
            updateCancelBtn.addEventListener('click', commentService.cancelUpdateMode);
        }

        const updateComment = function () {
            commentUpdateBtn.addEventListener('click', commentService.update)
        }

        const deleteComment = function () {
            commentList.addEventListener('click', commentService.remove);
        }

        const init = function () {
            addComment()
            updateMode()
            updateComment()
            deleteComment()
        }

        return {
            init: init
        }
    }

    const CommentService = function () {
        const add = function (event) {
            event.stopPropagation();
            const articleId = parseInt(document.getElementById('article-id').value);
            const commentContent = editor.getMarkdown();

            fetch('/comments', {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    articleId: articleId,
                    contents: commentContent
                })
            }).then(res => res.json())
                .then(createdComment => {
                    commentList.insertAdjacentHTML('beforeend', commentTemplate(createdComment));
                    editor.setMarkdown('');
                    commentCount.up();
                });

        }

        const updateMode = function (event) {
            event.stopPropagation();

            let updateBtn = event.target;
            if (updateBtn.getAttribute('data-btn-type') === 'update') {
                commentAddBtn.style.display = 'none';
                commentUpdateBtn.style.display = 'inline';
                updateCancelBtn.style.display = 'inline';

                let commentContainer = updateBtn.closest('li');
                let commentId = commentContainer.getAttribute('data-comment-id');
                let commentContents = commentContainer.querySelector('p').innerText;

                document.getElementById('comment-id').value = commentId;
                editor.setMarkdown(commentContents);
            }
        }

        const cancelUpdateMode = function (event) {
            event && event.stopPropagation();
            commentAddBtn.style.display = 'inline';
            commentUpdateBtn.style.display = 'none';
            updateCancelBtn.style.display = 'none';
            document.getElementById('comment-id').value = '';
            editor.setMarkdown('');
        }

        const update = function (event) {
            event.stopPropagation();

            let commentId = document.getElementById('comment-id').value;
            let selectedComment = commentList.querySelector(`li[data-comment-id="${commentId}"]`);

            fetch(`/comments/${commentId}`, {
                method: 'put',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    contents: editor.getMarkdown()
                })
            }).then(res => res.json())
                .then(updatedComment => {
                    selectedComment.querySelector('p').innerText = updatedComment.contents;
                    cancelUpdateMode();
                });
        }

        const remove = function (event) {
            event.stopPropagation();

            let updateBtn = event.target;
            if (updateBtn.getAttribute('data-btn-type') === 'delete') {
                let commentContainer = updateBtn.closest('li');
                let commentId = commentContainer.getAttribute('data-comment-id');

                fetch(`/comments/${commentId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    console.log(res.status);
                    if (res.ok) {
                        commentContainer.remove();
                        commentCount.down();
                    }
                });
            }
        }

        return {
            add: add,
            updateMode: updateMode,
            cancelUpdateMode: cancelUpdateMode,
            update: update,
            remove: remove
        }
    }

    const init = function () {
        const commentController = new CommentController()
        commentController.init()
    };

    return {
        init: init,
    };
})();

COMMENTAPP.init();
