const commentTemplate =
    `
        <li class="comment-item border bottom mrg-btm-30">
            <img class="thumb-img img-circle" src="https://avatars3.githubusercontent.com/u/50367798?v=4" alt="">
            <span href="" class="text-bold inline-block">{{commenter}}</span>
            <span class="sub-title inline-block pull-right">
                <i class="ti-timer pdd-right-5"></i>
                <span>{{minAgo}}</span>
            </span>
            <div class="view info">
                <p class="width-80 target">{{comment}}</p>
                <button data-comment-id="{{id}}" data-which-button="delete" type="submit" class="btn btn-default">
                    <i class="ti-close"></i>
                </button>
            </div>
            <div class="edit info">
                <input name="comment" type="text" value="{{comment}}">
                <button data-comment-id="{{id}}" data-which-button="update" type="submit" class="btn btn-default">
                    <i class="ti-save"></i>
                </button>
            </div>
         </li>
    `;

const compiledCommentTemplate = Handlebars.compile(commentTemplate);