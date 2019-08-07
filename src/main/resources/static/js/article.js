const articleApp = (function () {
    const articleId = document.getElementById('articleId').innerText;
    const email = document.getElementById('session-email').innerText;

    const ArticleEvent = function () {
        const articleService = new ArticleService();

        const save = function () {
            const saveButton = document.getElementById('save-btn');
            saveButton.addEventListener('click', articleService.save);
        };

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
            save();
            edit();
            update();
            deleteComment();
        };

        return {
            init: init
        }
    }
    const ArticleService = function () {
        const save = function () {
            const contents = document.getElementById('comment-contents').value;
            console.log(articleId);
            console.log(email);
            fetch("http://localhost:8080/articles/" + articleId + "/jsoncomments", {
                method: "POST",
                body: JSON.stringify({
                    email: email,
                    contents: contents,
                    articleId: articleId,
                }),
                headers: {
                    "Content-type": "application/json; charset=UTF-8",
                    "Accept": "application/json; charset=UTF-8"
                }
            })
                .then(response => response.json())
                .then(json => addTemplate(json))
}
        const prepareEdit = function(event) {
            if(event.target.classList.contains("ti-pencil")){
                const litag = event.target.closest('li')
                const ptag = litag.querySelector('p').innerText
                const input = litag.querySelector('input')
                const confirmButton = litag.querySelector('button')
                confirmButton.setAttribute("style","visibility:visible")
                confirmButton.setAttribute("style","visibility:visible");
                input.value = ptag
                input.setAttribute("style","visibility:visible")
                console.log("연필클릭")
            }
        }

        const update = function (event) {
            if(event.target.classList.contains("btn-icon")) {
                const litag = event.target.closest('li')
                const ptag = litag.querySelector('p')
                const confirmButton = litag.querySelector('button')
                const input = litag.querySelector('input')
                const updateCommentId = litag.querySelectorAll('div')[1].id;
                console.log("this is the id: " + updateCommentId);
                const updateCommentContents = input.value;
                const commenterEmail = litag.querySelector('span').innerText;
                console.log(commenterEmail);
                console.log("this is the contents: " + updateCommentContents);
                fetch("http://localhost:8080/articles/" + articleId + "/jsoncomments/" + updateCommentId, {
                    method: "PUT",
                    body: JSON.stringify({
                        email: commenterEmail,
                        contents: updateCommentContents,
                        articleId: articleId,
                        id: updateCommentId
                    }),
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Accept": "application/json; charset=UTF-8"
                    }
                })
                    .then(response => response.json())
                    .then(function (json) {
                        console.log(json)
                        input.setAttribute('style', "visibility:hidden");
                        confirmButton.setAttribute('style', "visibility:hidden");
                        ptag.innerText = json.contents;
                    });
            }
        }
        const deleteComment = function (event) {
            console.log("시작!");
            if(event.target.classList.contains("ti-trash")) {
                console.log("쓰레기통")
                const litag = event.target.closest('li');
                const deleteCommentId = litag.querySelectorAll('div')[1].id;
                const commenterEmail = litag.querySelector('span').innerText;
                fetch("http://localhost:8080/articles/" + articleId + "/jsoncomments/" + deleteCommentId, {
                    method: "DELETE",
                    body: JSON.stringify({
                        email: commenterEmail,
                        articleId: articleId,
                        id: deleteCommentId
                    }),
                    headers: {
                        "Content-type": "application/json; charset=UTF-8",
                        "Accept": "application/json; charset=UTF-8"
                    }
                })
                    .then(function () {
                        console.log("삭제왓음")
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