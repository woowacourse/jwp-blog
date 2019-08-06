function editBtnClickHandler(commentId) {
    const content = document.getElementById("comment-put-contents-" + commentId);
    content.innerHTML =
        `<textarea id='comment-put-contents'></textarea>  
         <button id='comment-put-button' data-comment-id='${commentId}'  
         name='comment-put' value='수정' type='submit'>수정</button>`
}


const articleId = document.getElementById('articleId').value;
const baseUrl = "/api/articles/" + articleId + "/comments/";

const postButton = document.querySelector("#comment-post-button");
const commentSection = document.getElementById("comment-section")

postButton.addEventListener("click", postComment);

// 등록
function postComment() {
    const contents = document.getElementById('comment-post-contents').value;

    fetch(baseUrl, {
        method: 'POST',
        headers: {
            'Accept': 'application/json; charset=UTF-8',
            'Content-Type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify({
            contents: contents
        }),
        credentials: "include"
    })
        .then(res => res.json())
        .then(json => {
            printComments()
        })
        .catch(err => console.log(err))
}

// 조회
const printComments = function printComments(e) {
    fetch(baseUrl, {
        method: 'GET',
        headers: {
            'Accept': 'application/json; charset=UTF-8',
            'Content-Type': 'application/json; charset=UTF-8',
        },
    })
        .then(res => res.json())
        .then(comments => {
            let node = document.getElementById('comment-section');
            while (node.firstChild) {
                node.removeChild(node.firstChild);
            }

            document.getElementById('comments-size').innerText = comments.length;

            comments.forEach(function (comment) {
                console.log(comment)
                node.insertAdjacentHTML('beforeend',
                    template({
                        "contents": comment.contents,
                        "id": comment.id,
                        "commenter": comment.name,
                        "modifiedDate": comment.modifiedDate
                    }))
            })

        })
        .catch(err => console.log(err))
};

const template = (comment) => {
    return `<li class="comment-item border bottom mrg-btm-30">
                <img class="thumb-img img-circle" src="https://avatars2.githubusercontent.com/u/3433096?v=4" alt="">
                <div class="info">
                    <span href="" class="text-bold inline-block">${comment.commenter}</span>
                    <span class="sub-title inline-block pull-right">
                    <div>
                         <i class="ti-timer pdd-right-5"></i>
                        <span class="elapsed-time">${comment.modifiedDate}</span>
                    </div>
                                        
                    <button class="btn btn-icon float-right pointer" id="comment-modify-form-button" type="submit" 
                            onclick="editBtnClickHandler(${comment.id})">
                        <i class="ti-pencil text-dark font-size-16 pdd-horizontal-5"></i>
                    </button>  
                    
                      <button class="btn btn-icon float-right pointer" name="comment-delete" data-comment-id="${comment.id}" type="submit">
                           
                         <i class="ti-trash text-dark font-size-16 pdd-horizon-5"></i>
                      </button>
                      </span>              
                </div>
                
              
                 <p class="width-80" id="comment-put-contents-${comment.id}">${comment.contents}</p>
            </li>`
};


commentSection.addEventListener("click", function (event) {
        // 수정
        if (event.target.name === 'comment-put') {
            const contents = document.getElementById('comment-put-contents').value;
            fetch(baseUrl + event.target.dataset.commentId, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json; charset=UTF-8',
                    'Content-Type': 'application/json; charset=UTF-8',
                },
                body: JSON.stringify({
                    contents: contents
                }),
                credentials: "include"
            })
                .then(res => res.json())
                .then(commentResponse => {
                    printComments();
                })
                .catch(err => console.log(err))
        }

        //삭제
        if (event.target.name === 'comment-delete') {
            fetch(baseUrl + event.target.dataset.commentId, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json; charset=UTF-8',
                },
                credentials: "include"
            })
                .then(res => res.json())
                .then(json => {
                    printComments()
                })
                .catch(err => contents.log(err))
        }
    }
);

