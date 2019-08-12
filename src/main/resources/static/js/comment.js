const articleId = document.getElementById("articleId").value;
let commentList;

document.getElementById("comment-create").addEventListener('click', function () {
    const data = {contents: document.getElementById('comment-contents').value};
    postData('/articles/' + articleId + '/comment', data)
        .then(data => {
            commentList = data;
            print(commentList)
            return JSON.stringify(data);
        })
        .catch(error => console.error(error));
});

function postData(url, data) {
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json());
}

function deleteData(url, data) {
    return fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json());
}

function deleteComment(commentId) {
    deleteData('/articles/' + articleId + '/comment/' + commentId)
        .then(data => {
            print(data)
            return JSON.stringify(data);
        })
        .catch(error => console.error(error));
}

function putData(url, data) {
    return fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => response.json());
}

function updateComment(commentId) {
    let contents = {contents: document.getElementById("comment" + commentId + "text").value};
    putData('/articles/' + articleId + '/comment/' + commentId, contents)
        .then(data => {
            print(data)
            return JSON.stringify(data);
        })
        .catch(error => console.error(error));
}

function update(id) {
    const temp = "comment" + id;
    const x = document.getElementById(temp);
    x.style.display = "block";
}

function print(comments) {
    let i;
    let template = '';
    for (i = 0; i < comments.length; i++) {
        template = template +
            `<li class="comment-item border bottom mrg-btm-30" >
                <img alt="" 
                    class="thumb-img img-circle" 
                    src="https://avatars3.githubusercontent.com/u/50367798?v=4">
                <div class="info">
                    <span class="text-bold inline-block">${comments[i].author.userName}</span>
                    <span class="sub-title inline-block pull-right">
                    <i class="ti-timer pdd-right-5"></i>
                    <span>6 min ago</span>
                    </span>
                    
                    <p class="width-80" >${comments[i].contents}</p>
                    <div id = comment${comments[i].id} style="display: none;">
                        <form method="post" action=/articles/${articleId}/comment/${comments[i].id}>
                            <input name="_method" type="hidden" value="put">
                            <input id=comment${comments[i].id}text value=${comments[i].contents} name="contents" class="text-area" type="text" style="border: 0; background-color: #fff; width: 80%; height: 100px; border-radius: 10px; margin-bottom: 10px">
                            <button onclick=updateComment(${comments[i].id}) class="btn btn-info float-right mrg-btm-0 relative bottom-5" type="button">수정</button>
                        </form>
                    </div>
                    
                    <div>
                        <button class="pointer btn btn-icon" style="float: left;"\n onclick=update(${comments[i].id})>
                            <i class="ti-pencil text-dark font-size-10 pdd-horizontal-5"></i>
                        </button>
                        <form method="post" action=/articles/${articleId}/comment/${comments[i].id}>
                            <input name="_method" type="hidden" value="delete">
                            <button class="pointer btn btn-icon" style="float: left;" type="button" onclick=deleteComment(${comments[i].id})>
                                <i class="ti-trash text-dark font-size-10 pdd-horizontal-5"></i>
                            </button>
                        </form>
                    </div>
                    <p id="comment-edit-section" style="clear: both;"></p>
                </div>
            </li>`;
    }
    document.getElementById('comment-list').innerHTML = template;
}