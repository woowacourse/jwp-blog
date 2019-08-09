const articleApp = (function () {
    const articleId = document.getElementById('articleId').innerText;

    const header = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }

    const ArticleEvent = function () {
        const articleService = new ArticleService();

        const save = function () {
            const saveButton = document.getElementById('save-btn');
            saveButton.addEventListener('click', articleService.save)
        }
        const edit = function () {
            const divSection = document.getElementById("comment-div");
            divSection.addEventListener('click', articleService.prepareEdit);
        }
        const update = function () {
            const divSection = document.getElementById("comment-div");
            divSection.addEventListener('click', articleService.update);
        }
        const deleteComment = function () {
            const divSection = document.getElementById("comment-div");
            divSection.addEventListener('click', articleService.deleteComment);
        }
        const init = function () {
            save()
            edit()
            update()
            deleteComment()
        }

        return {
            init: init
        }
    }
    const ArticleService = function () {
        const save = function () {
            const contents = document.getElementById('comment-contents').value;
            fetch("http://localhost:8080/articles/" + articleId + "/comments", {
                method: "POST",
                body: JSON.stringify({
                    contents: contents,
                    articleId: articleId,
                }),
                headers: header
            }).then(response => response.json())
                .then(json => addTemplate(json))
        }
        const prepareEdit = function (event) {
            if (event.target.id === "pencil") {
                const litag = event.target.closest('li')
                const ptag = litag.querySelector('p').innerText
                console.log(ptag)
                const input = litag.querySelector('input')
                const confirmButton = litag.querySelectorAll('button')[2]
                console.log(confirmButton.id)
                confirmButton.setAttribute("style", "visibility:visible")
                input.value = ptag
                input.setAttribute("style", "visibility:visible")
            }
        }

        const update = function (event) {
            if (event.target.id === "save-confirm") {
                const litag = event.target.closest('li')
                const ptag = litag.querySelector('p')
                const confirmButton = litag.querySelectorAll('button')[2]
                const input = litag.querySelector('input')
                const updateCommentId = litag.getAttribute("data-comment-id")
                const updateCommentContents = input.value;
                fetch("/articles/" + articleId + "/comments/" + updateCommentId, {
                    method: "PUT",
                    body: JSON.stringify({
                        contents: updateCommentContents,
                        id: updateCommentId
                    }),
                    headers: header
                })
                    .then(response => response.json())
                    .then(function (json) {
                        input.setAttribute('style', "visibility:hidden");
                        confirmButton.setAttribute('style', "visibility:hidden");
                        ptag.innerText = json.contents;
                });
            }
        }

        const deleteComment = function (event) {
            if (event.target.id === "trash") {
                const litag = event.target.closest('li');
                const deleteCommentId = litag.querySelectorAll('div')[1].id;
                fetch("http://localhost:8080/articles/" + articleId + "/comments/" + deleteCommentId, {
                    method: "DELETE",
                    headers: header
                })
                    .then(function () {
                        litag.remove()
                    });
            }
        }
        return {
            save: save,
            prepareEdit: prepareEdit,
            update: update,
            deleteComment: deleteComment
        }
    }
    const init = function () {
        const articleEvent = new ArticleEvent();
        articleEvent.init();
    };
    return {
        init: init,
    };
})();
articleApp.init();