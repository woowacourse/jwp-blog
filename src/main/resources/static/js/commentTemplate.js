function addTemplate (json) {
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
    const updateTemplate = "<button type=\"button\" id= '" + updateId + "' class=\"float-right pointer btn btn-icon\">"
        + "<i class=\"ti-pencil text-dark font-size-16 pdd-horizontal-5\"></i>"
        + "</button>"
        + "<button id= '" + deleteId + "' class=\"float-right pointer btn btn-icon\">"
        + "<i class=\"ti-trash text-dark font-size-16 pdd-horizontal-5\"></i>"
        + "</button>";

    const commentDiv = document.getElementById("comment-div");
    commentDiv.insertAdjacentHTML("beforeend", template);
    const visibleButtons = document.getElementById(commentId);
    if (isValidUser) {
        visibleButtons.insertAdjacentHTML("afterbegin", updateTemplate);
    }
}