const CommentApp = (function () {

    const CommentController = function () {
        const commentService = new CommentService()

        const deleteComment = function () {
            const list = document.getElementById('comment-list');
            list.addEventListener('click', commentService.remove);
        };

        const init = function () {
            deleteComment();
        };

        return {
            init: init
        }
    }

    const CommentService = function () {
        const remove = function (event) {
            const articleId = document.getElementById('article-id').value;
            const btn = event.target;
            if (btn.id !== 'delete-btn') {
                return;
            }
            const form = document.querySelector('form');
            form.action = '/articles/' + articleId +  '/comment/' + btn.value;
            const deleteInput = document.createElement('input');
            deleteInput.type = 'hidden';
            deleteInput.name = '_method';
            deleteInput.value = 'delete';

            form.insertAdjacentElement('afterbegin', deleteInput);

            form.submit();
        };

        return {
            remove: remove,
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

CommentApp.init();