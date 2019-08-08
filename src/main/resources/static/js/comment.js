const CommentApp = (function () {

    const CommentController = function () {
        const commentService = new CommentService()

        const deleteComment = function () {
            const list = document.getElementById('comment-list');
            list.addEventListener('click', commentService.remove);
        };

        const updateComment = function () {
            const list = document.getElementById('comment-list');
            list.addEventListener('click', commentService.update);
        };

        const init = function () {
            updateComment();
            deleteComment();
        };

        return {
            init: init
        }
    };

    const CommentService = function () {
        const remove = function (event) {
            const btn = event.target;
            if (btn.classList.contains('delete-comment')) {
                deleteComment(event);
            }
        };

        const deleteComment = function (event) {
            const btn = event.target;
            const url = '/comments/' + btn.value;
            const obj = {
                method: 'DELETE',
                headers: {
                    "Content-type": "application/json; charset=UTF-8"
                }
            };

            fetch(url, obj)
                .then(function (response) {
                    return response.json();
                })
                .then(function (json) {
                    const li = btn.parentNode;
                    const ul = btn.parentNode.parentNode;
                    ul.removeChild(li);
                });
        };

        const update = function (event) {
            const btn = event.target;
            if (btn.classList.contains("btn-lg")) {
                const url = '/comments/' + btn.value;
                document.getElementById('comment-update-url').setAttribute('value', url);
            }
        };

        return {
            remove: remove,
            update: update,
        }
    };

    const init = function () {
        const commentController = new CommentController();
        commentController.init();
    };

    return {
        init: init,
    };
})();

CommentApp.init();