function addTemplate(comment) {
    const template = `<li class="comment-item border bottom mrg-btm-30" data-comment-id="${comment.id}">
        <img class="thumb-img img-circle" src="https://avatars2.githubusercontent.com/u/3433096?v=4" alt="">
        <div class="info">
        <span href="" class="text-bold inline-block">${comment.authorName}</span>
        <span class="sub-title inline-block pull-right">
        <div>
        <button id="pencil" class="float-right pointer btn btn-icon">
        <i class="ti-pencil text-dark font-size-16 pdd-horizontal-5"></i>
        </button>
        <button id="trash" class="float-right pointer btn btn-icon">
        <i class="ti-trash text-dark font-size-16 pdd-horizontal-5"></i>
        </button>
        <button id="save-confirm"class="float-right pointer btn btn-icon" style="visibility: hidden">저장확인</button>
        <input type="text" id="content-input1" style="visibility:hidden">
        </div>
        <i class="ti-timer pdd-right-5"></i>
        <span>6 min ago</span>
        </span>
        <p class="width-80">${comment.contents}</p>
        </div>
        </li>`
    const commentUl = document.getElementById("comment-div");
    commentUl.insertAdjacentHTML("beforeend", template);
}