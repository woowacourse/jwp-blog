const CommentApp = (function () {

    const CommentController = function () {
        const commentService = new CommentService()

        const deleteComment = function () {
            const list = document.getElementById('comment-list');
            list.addEventListener('click', commentService.remove);
            list.addEventListener('click',commentService.update);
        };

        const init = function () {
            deleteComment();
        };

        return {
            init: init
        }
    }

    const CommentService = function () {
        const DOMAIN_URL = location.protocol + "//" + location.host;

        const remove = function (event) {
            console.log("hello")
            const articleId = document.getElementById('article-id').value;
            const btn = event.target;
            if (btn.classList.contains('delete-comment')) {
                deleteComment(event);
            }
        };

        const deleteComment = function (event) {
            const articleId = document.getElementById('article-id').value;
            const btn = event.target;
            console.log(btn.parentNode)
            const url = '/articles/' + articleId + '/comment/' + btn.value;
            const URL = DOMAIN_URL + url;
            console.log(URL);
            const obj = {
                method: 'DELETE',
                headers: {
                    "Content-type": "application/json; charset=UTF-8"
                }
            };

            fetch(URL, obj)
                .then(function (response) {
                    return response.json();
                })
                .then(function (json) {
                    const li = btn.parentNode
                    const ul = btn.parentNode.parentNode
                    ul.removeChild(li)
                });
        }

        const update = function(event) {
            const articleId = document.getElementById('article-id').value;
            const btn = event.target;
            if(btn.classList.contains("btn-lg")) {
                const url = '/articles/' + articleId + '/comment/' + btn.value;
                console.log(url);
                document.getElementById('comment-update-url').setAttribute('value', url);
            }
        }

        return {
            remove: remove,
            update: update
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