const articleApp = (function () {
    const articleId = document.getElementById('articleId').innerText;
    const email = document.getElementById('session-email').innerText;

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
        const init = function () {
            save()
            edit()
            update()
        }

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
                .then(function (json) {
                    console.log("inside the function: ");
                    console.log(json);
                    const email = json.email;
                    const commentId = json.id;
                    const contents = json.contents;
                    const articleId = json.articleId;
                    const isValidUser = json.validUser;
                    console.log("json_email: " + email);
                    console.log("json_contents: " + contents);
                    console.log("json_commentId: " + commentId);
                    console.log("json_articleId: " + articleId);
                    console.log("json_isvaliduser: " + isValidUser);

                    const template = "<ul class=\"list-unstyled list-info\" id=>"
                        + "<li class=\"comment-item border bottom mrg-btm-30\">"
                        + "<img class=\"thumb-img img-circle\" src=\"https://avatars2.githubusercontent.com/u/3433096?v=4\" alt=\"\">"
                        + "<div class=\"info\">"
                        + "<span href=\"\" class=\"text-bold inline-block\">" + email + "</span>"
                        + "<span class=\"sub-title inline-block pull-right\">"
                        + "<button class=\"float-right pointer btn btn-icon\" style=\"visibility: hidden\">저장확인</button>"
                        + "<input type=\"text\" id=\"content-input1\" style=\"visibility:hidden\">"
                        + "<div id= '" + commentId + "'  ></div>"
                        + "<i class=\"ti-timer pdd-right-5\"></i>"
                        + "<span>6 min ago</span>"
                        + " </span>"
                        + "<p class=\"width-80\"> " + contents + "</p>"
                        + "</div>"
                        + "</li>"
                        + "</ul>"
                    const updateId = "update-" + commentId;
                    const deleteId = "delete-" + commentId;
                    const updateTemplate = "<button type=\"button\" id= '"+ updateId +"' class=\"float-right pointer btn btn-icon\">"
                        + "<i class=\"ti-pencil text-dark font-size-16 pdd-horizontal-5\"></i>"
                        + "</button>"
                        + "<button id= '"+ deleteId +"' class=\"float-right pointer btn btn-icon\">"
                        + "<i class=\"ti-trash text-dark font-size-16 pdd-horizontal-5\"></i>"
                        + "</button>";

                    const commentDiv = document.getElementById("comment-div");
                    commentDiv.insertAdjacentHTML("beforeend", template);
                    const visibleButtons = document.getElementById(commentId);
                    if (isValidUser) {
                        visibleButtons.insertAdjacentHTML("afterbegin", updateTemplate);
                    }
                })
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
            const litag =event.target.closest('li')
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
                });

        }
        return {
            save: save,
            prepareEdit: prepareEdit,
            update: update
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