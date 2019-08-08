const articleId = document.getElementById("articleId").value;
let commentList;

document.getElementById("comment-create").addEventListener('click', function () {
    const data = {contents: document.getElementById('comment-contents').value};
    postData('/articles/' + articleId + '/comment', data)
        .then(data => {
            console.log("*********")
            console.log(data)
            commentList = data;
            print(commentList)
            return JSON.stringify(data);
        })
        .catch(error => console.error(error));
});

function postData(url, data) {
    return fetch(url, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
        },
        redirect: 'follow',
        referrer: 'no-referrer',
        body: JSON.stringify(data),
    })
        .then(response => response.json());
}

function deleteData(url, data) {
    return fetch(url, {
        method: 'DELETE',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
        },
        redirect: 'follow',
        referrer: 'no-referrer',
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
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json',
        },
        redirect: 'follow',
        referrer: 'no-referrer',
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
    var x = document.getElementById(temp);
    x.style.display = "block";
}

function print(comments) {
    var i;
    var template = '';
    for (i = 0; i < comments.length; i++) {
        template = template + '<li class="comment-item border bottom mrg-btm-30" >\n' +
            '<img alt=""\n' +
            'class="thumb-img img-circle"\n' +
            'src="https://avatars3.githubusercontent.com/u/50367798?v=4">\n' +
            '<div class="info">\n' +
            '<span class="text-bold inline-block"\n' +
            `>${comments[i].author.userName}</span>\n` +
            '<span class="sub-title inline-block pull-right">\n' +
            '<i class="ti-timer pdd-right-5"></i>\n' +
            '<span>6 min ago</span>\n' +
            '\n' +
            '</span>\n' +
            `<p class="width-80" >${comments[i].contents}</p>\n` +
            `<div id = comment${comments[i].id} style="display: none;">\n` +
            '<form method="post"\n' +
            `action=/articles/${articleId}/comment/${comments[i].id}>\n` +
            '<input name="_method" type="hidden" value="put">\n' +
            `<input id=comment${comments[i].id}text value=${comments[i].contents} name="contents" class="text-area"\n` +
            'type="text"\n' +
            'style="border: 0; background-color: #fff; width: 80%; height: 100px; border-radius: 10px; margin-bottom: 10px">\n' +
            `<button onclick=updateComment(${comments[i].id}) class="btn btn-info float-right mrg-btm-0 relative bottom-5"\n` +
            'type="button">수정\n' +
            '</button>\n' +
            '</form>\n' +
            '</div>\n' +
            '<div>\n' +
            `<button class="pointer btn btn-icon" style="float: left;"\n onclick=update(${comments[i].id})>\n` +
            '<i class="ti-pencil text-dark font-size-10 pdd-horizontal-5"></i>\n' +
            '</button>\n' +
            // '<form method="post"\n' +
            // `action=/articles/${articleId}/comment/${comments[i].id}>\n` +
            '<input name="_method" type="hidden" value="delete">\n' +
            `<button class="pointer btn btn-icon" style="float: left;" type="button" onclick=deleteComment(${comments[i].id})>\n` +
            '<i class="ti-trash text-dark font-size-10 pdd-horizontal-5"></i>\n' +
            '</button>\n' +
            // '</form>\n' +
            '</div>\n' +
            '<p id="comment-edit-section" style="clear: both;"></p>\n' +
            '</div>\n' +
            '</li>';
    }
    document.getElementById('comment-list').innerHTML = template;
}