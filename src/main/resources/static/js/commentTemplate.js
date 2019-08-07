function commentButtonTemplate() {
    let buttonTemplate = `
            <button class="btn btn-icon float-right pointer comment-del-button"
                    type="button">
                <i class="ti-trash text-dark font-size-16 pdd-horizon-5 comment-del-button"></i>
            </button>
        
        <button class="comment-edit float-right pointer btn btn-icon">
            <i class="comment-edit ti-pencil text-dark font-size-16 pdd-horizontal-5"></i>
        </button>`;

    return buttonTemplate
}

function commentTemplate(commentDto, buttonTemplate) {

    let template = `
        <li class="comment-item border bottom mrg-btm-30" data-comment-id ="${commentDto.id}">
            <img alt=""
                 class="thumb-img img-circle"
                 src="https://avatars2.githubusercontent.com/u/3433096?v=4">
            <div class="info">
                <span class="text-bold inline-block" >${commentDto.userName}</span>

                <span class="sub-title inline-block pull-right">
        <i class="ti-timer pdd-right-5"></i>p
        <span>6 min ago</span>
    </span>
                ${buttonTemplate}
                <p class="width-80">${commentDto.contents}</p>
            </div>
        </li>`;

    return template
}

function commentEditTemplate(contentsId, editorId, buttonId) {
    const template = `<div class="add-comment edit-comment-form">
                    <input id="${contentsId}" name="contents" type="hidden">
                    <div id="${editorId}" class = "comment-editor"></div>
                    <button class="btn btn-default comment-edit-finish-button" id="${buttonId}" type="button">댓글 수정
                    </button></div>
                    `;

    return template
}

