const template =
    `<li class="comment-item border bottom mrg-btm-30">
        <img class="thumb-img img-circle" src="https://avatars3.githubusercontent.com/u/50367798?v=4" alt="">
        <div class="info">
            <a class="text-bold inline-block" href="/mypage/{{author.id}}">{{author.name.name}}</a>
                <a class="inline-block sub-title pull-right">
                    <i class="ti-trash text-dark font-size-16 pdd-horizontal-5 delete-comment-btn"></i>
                </a>
                <a class="inline-block sub-title pull-right">
                    <i class="ti-pencil text-dark font-size-16 pdd-horizontal-5 edit-comment-btn"></i>
                </a>
            <p id="comment{{id}}" class="width-80 tempComment" style="display: block">{{contents}}</p>
        <input id="change{{id}}" class="width-80 tempInput" style="display: none;">
        </div>
    </li>`;

const host = 'http://' + window.location.host;

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
            putData('PUT', host + '/articles/' + articleId + '/comments/' + editId, updateComment)
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
        putData('DELETE', host + '/articles/' + articleId + '/comments/' + editId)
            .then(data => initComments(data))
            .catch(data => console.log(data))

    }
}

function initComments(data) {
    commentList.innerHTML = "";
    data.forEach(x => {
        refresh(x);
    });
    addCommentCount(data);
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

function addSave(event) {
    const articleId = window.location.pathname.split("/")[2];
    const commentContents = document.getElementById('comment-contents');
    const comments = {
        contents: commentContents.value
    };

    putData('POST', host + '/articles/' + articleId + '/comments', comments)
        .then(data => initComments(data))
        .catch(data => console.log(data));
    editor.setValue('');
    commentContents.value = '';
}

function addCommentCount(data) {
    const commentCount = document.getElementById("comment-count");
    commentCount.innerText = data.length;
}

const saveBtn = document.getElementById('save-btn');
saveBtn.addEventListener('click', addSave);
commentList.addEventListener('click', editBtnClickHandler);
commentList.addEventListener('keyup', cancelEditHandler);