const articleApp = (function () {
    const articleId = document.getElementById('articleId').innerText;
    const email = document.getElementById('session-email').innerText;
    const updateTemplate = "<button type=\"button\" class=\"float-right pointer btn btn-icon\">"
        + "<i class=\"ti-pencil text-dark font-size-16 pdd-horizontal-5\"></i>"
        + "</button>"
        + "<button type=\"submit\" class=\"float-right pointer btn btn-icon\">"
        + "<i class=\"ti-trash text-dark font-size-16 pdd-horizontal-5\"></i>"
        + "</button>";
    const ArticleEvent = function () {
        const articleService = new ArticleService();

        const save = function () {
            const saveButton = document.getElementById('save-btn');
            saveButton.addEventListener('click', articleService.save)
        }
        const init = function () {
            save()
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
                        + "<input type=\"text\" id=\"content-input1\" style=\"display:none\">"
                        + "<span href=\"\" class=\"text-bold inline-block\">" + email + "</span>"
                        + "<span class=\"sub-title inline-block pull-right\">"
                    + "<div id= '"+ commentId + "'  ></div>"
                        + "<i class=\"ti-timer pdd-right-5\"></i>"
                        + "<span>6 min ago</span>"
                        + " </span>"
                        + "<p  class=\"width-80\"> "+ contents +"</p>"
                        + "</div>"
                        + "</li>"
                        + "</ul>"
                    const commentDiv = document.getElementById("comment-div");
                    commentDiv.insertAdjacentHTML("beforeend", template);
                    const visibleButtons = document.getElementById(commentId);
                    if (isValidUser) {
                        visibleButtons.insertAdjacentHTML("afterbegin", updateTemplate);
                    }
                })
        }
        return {
            save: save
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