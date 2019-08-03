const template = '<li class="comment-item border bottom mrg-btm-30">\n' +
    '<img class="thumb-img img-circle"\n' +
    'src="https://avatars3.githubusercontent.com/u/50367798?v=4" alt="">\n' +
    '<div class="info">\n' +
    '<a class="text-bold inline-block"\n' +
    'href="/mypage/{{userId}}"\n>{{userName}}</a>\n' +
    // '<form action="/articles/{{articleId}}/comments/{{commentId}}"\n' +
    // 'method="post">\n' +
    // '<input type="hidden" name="_method" value="delete">'+
    '<button class="inline-block sub-title pull-right"\n' +
    '">\n' +
    '<i class="ti-trash text-dark font-size-16 pdd-horizontal-5 delete-comment-btn"></i>\n' +
    '</button>\n' +
    // '</form>\n' +
    '<a class="inline-block sub-title pull-right">\n' +
    '<i class="ti-pencil text-dark font-size-16 pdd-horizontal-5 edit-comment-btn"></i>\n' +
    '</a>\n' +
    '<p id="comment{{id}}" class="width-80 tempComment" style="display: block"\n' +
    '>{{contents}}</p>\n' +
    '<input id="change{{id}}" class="width-80 tempInput"\n' +
    'style="display: none;">\n' +
    '</div>\n' +
    '</li>'

const commentTemplate = Handlebars.compile(template);

const commentList = document.getElementById("comment-list");

function editBtnClickHandler(event) {
    const comment = event.target.parentElement.parentElement.parentElement;
    const articleId = window.location.pathname.split("/")[2];
    if (event.target.classList.contains("edit-comment-btn")) {
        const editDiv = getContents(comment);
        const editAble = getInputContents(comment);
        const editId = editAble.id.substring(6, editAble.id.length);
        if (editDiv.style.display === "none") {
            editDiv.style.display = "block";
            const updateComment = {
                contents: editAble.value,
            }
            putData('PUT', 'http://localhost:8080/ajax/articles/' + articleId + '/comments/' + editId, updateComment)
                .then(data => initComments(data))
                .catch(data => console.log(data))
            editAble.style.display = "none";
        } else {
            editDiv.style.display = "none";
            editAble.style.display = "block";
            editAble.value = editDiv.innerText;
        }
    }

    if (event.target.classList.contains("delete-comment-btn")) {
        const editAble = getInputContents(comment);
        const editId = editAble.id.substring(6, editAble.id.length);
        putData('DELETE', 'http://localhost:8080/ajax/articles/' + articleId + '/comments/' + editId)
            .then(data => initComments(data))
            .catch(data => console.log(data))

    }
}

function initComments(data) {
    commentList.innerHTML = "";
    data.forEach(x => {
        refresh(x);
    });
}

function cancelEditHandler(event) {
    const comment = event.target.parentElement.parentElement.parentElement;
    const editDiv = getContents(comment);
    const editAble = getInputContents(comment);
    if (event.key === "Escape" && event.target.classList.contains('tempInput')) {
        editDiv.style.display = "block";
        editAble.style.display = "none";
    }
}

function getContents(comment) {
    return comment.getElementsByClassName('tempComment')[0];
}

function getInputContents(comment) {
    return comment.getElementsByClassName('tempInput')[0];
}

function putData(method, url, data) {
    return fetch(url, {
        method: method, // *GET, POST, PUT, DELETE, etc.
        mode: 'cors', // no-cors, cors, *same-origin
        cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
        credentials: 'same-origin', // include, *same-origin, omit
        headers: {
            'Content-Type': 'application/json',
        },
        redirect: 'follow', // manual, *follow, error
        referrer: 'no-referrer', // no-referrer, *client
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => response.json()); // parses JSON response into native JavaScript objects
}

function refresh(commentData) {
    commentList.insertAdjacentHTML("beforeend", commentTemplate(commentData))
}

commentList.addEventListener('click', editBtnClickHandler);
commentList.addEventListener('keyup', cancelEditHandler);