let page = 0;

function getAllBoardList(){
    $.ajax({
        url: "/api/board/all?page=" + page,
        type: "get",
        dataType: "json"
    }).done(res=>{
        console.log(res.data.content)
        res.data.content.forEach(data=>{
            let list = '<div class="img-box" id="board-'+ data.id +'">\n' +
                '<img src="/api/board/image/?username='+ data.userDTO.username + "&fileName=" + data.imageUrl + "&boardId=" + data.id +'" />\n' +
                '<div class="comment" onclick="javascript:boardModalOpen('+ data.id + "," + data.userDTO.id +')">' +
                '<a href="#" class=""> <i class="fas fa-heart"></i><span>' + data.likeCount + '</span>\n' +
                '<i class="fas fa-comment"></i><span id="commentIcon">' + data.replyList.length + '</span>\n' +
                '</a>\n' +
                '</div>'+
                '</div>'

            $(".tab-1-content-inner").append(list);
        })
    }).fail(error=>{
        console.log(error, "getAllBoardList Api error");
    });
}

getAllBoardList();

$(window).scroll(() => {

    let scroll = $(window).scrollTop() - ($(document).height() - $(window).height())

    console.log(scroll)

    if(scroll < 1 && scroll > -1){
        page++;
        getAllBoardList();
    }
});

function boardModalOpen(boardId, userId) {

    $(".modal-board").css("display", "flex");
    $("body").css("overflow", "hidden")
    $(".header").css("overflow", "hidden")
    $(".board-img").prepend($("#board-" + boardId).children()[0])

    $.ajax({
        url: "/p/board/" + boardId,
        type: "get",
        dataType: "json"
    }).done(res=>{
        console.log(res.data);
        $("#boardId-").attr("id", "boardId-" + res.data.id);
        let likeIcon = $("#modalLikeIcon").attr("id", "modalLikeIcon-" + res.data.id)
        $(".modal-heart-icon").attr("onclick", "toggleLike(" + res.data.id + ")")
        let username = res.data.userDTO.username;
        let profileImage = res.data.userDTO.profileImage;
        $("#boardUserUsername").prepend(res.data.userDTO.username)
        $("#boardUserImg").prepend('<img class="profile-image" src="/api/image/?username='+ username +'&fileName='+ profileImage +'" onerror="/images/non.jpg">')
        $(".like").prepend("<b id='modalLikeCount'>????????? " + res.data.likeCount + "???</b>")
        $(".betweenTime-time").prepend(res.data.time)
        $(".sl__item__input").append('<input type="text" placeholder="?????? ??????..." id="storyCommentInput-'+ res.data.id +'" />\n'+'<button type="button" onClick="addComment('+ res.data.id +')">??????</button>');
        if(res.data.likeCheck === 1){
            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }

        let item = '<div class="board-modal-item" id="board-modal-item-comment-'+ res.data.id +'">' +
            '<div class="p-board-user-img" id="commentUserImg-'+ res.data.id +'">' +
            '<img class="profile-image" src="/api/image/?username='+ res.data.userDTO.username +'&fileName='+ res.data.userDTO.profileImage +'" onerror="/images/non.jpg">' +
            '</div>' +
            '<div class="p-board-modal-item">'+
            '<div class="p-board-user-username" id="modal-user-Username-'+ res.data.id +'">'+
            res.data.userDTO.username+
            '</div>'+
            '<div class="p-board-user-comment" id="modal-user-comment-'+ res.data.id +'">'+
            res.data.content+
            '</div>'+
            '</div>'+
            '</div>';

        $(".board-modal-list__ul").prepend(item);

        res.data.replyList.forEach((obj)=> {

            item = '<div class="board-modal-item" id="board-modal-item-comment-'+ obj.replyId +'">' +
                '<div class="p-board-user-img" id="commentUserImg-'+ obj.replyId +'">' +
                '<img class="profile-image" src="/api/image/?username='+ obj.username +'&fileName='+ obj.profileImage +'" onerror="/images/non.jpg">' +
                '</div>' +
                '<div class="p-board-modal-item">'+
                '<div class="p-board-user-username" id="modal-user-Username-'+ obj.replyId +'">'+
                obj.username+
                '</div>'+
                '<div class="p-board-user-comment" id="modal-user-comment-'+ obj.replyId +'">'+
                obj.content+
                '</div>'+
                '</div>'+
                '<button type="button" onclick="deleteComment('+ obj.replyId +')">\n' +
                '<i class="fas fa-times"></i>\n' +
                '</button>\n'+
                '</div>';

            $(".board-modal-list__ul").append(item);
        })

    }).fail(error=>{
        console.log(error);
    })
    $(".p-board-user-img").prepend()
}

function toggleLike(boardId) {
    let likeIcon = $("#modalLikeIcon-" + boardId);
    let likeCount = $("#modalLikeCount")

    if (likeIcon.hasClass("far")) {
        $.ajax({
            url: "/api/like/board/"+ boardId,
            type: "post",
            data: "",
            dataType: "json"
        }).done(res=>{
            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
            likeCount.empty().prepend("????????? " + res.data + "???");
            console.log(res, "????????? api ??????")
        }).fail(error=>{
            console.log(error, "????????? api ??????")
        });
    } else {
        $.ajax({
            url: "/api/like/board/" + boardId,
            type: "delete",
            data: "",
            dataType: "json"
        }).done(res=>{
            likeIcon.removeClass("fas");
            likeIcon.removeClass("active");
            likeIcon.addClass("far");
            likeCount.empty().prepend("????????? " + res.data + "???");
            console.log(res, "????????? ?????? api ??????")
        }).fail(error=>{
            console.log(error, "????????? ?????? api ??????")
        });
    }
}

function addComment(boardId) {

    let commentInput = $("#storyCommentInput-" + boardId);
    let commentList = $(".board-modal-list__ul");

    let data = {
        content: commentInput.val()
    }

    console.log(data)

    if (data.content === "") {
        alert("????????? ??????????????????!");
        return;
    }

    $.ajax({
        url: "/api/reply/board/" + boardId,
        type: "post",
        data: data,
        dataType: "json"
    }).done(res=>{
        console.log(res);
        let content = '<div class="board-modal-item" id="board-modal-item-comment-'+ res.data.replyId +'">' +
            '<div class="p-board-user-img" id="commentUserImg-'+ res.data.replyId +'">' +
            '<img class="profile-image" src="/api/image/?username='+ res.data.username +'&fileName='+ res.data.profileImage +'" onerror="/images/non.jpg">' +
            '</div>' +
            '<div class="p-board-modal-item">'+
            '<div class="p-board-user-username" id="modal-user-Username-'+ res.data.replyId +'">'+
            res.data.username+
            '</div>'+
            '<div class="p-board-user-comment" id="modal-user-comment-'+ res.data.replyId +'">'+
            res.data.content+
            '</div>'+
            '</div>'+
            '<button type="button" onclick="deleteComment('+ res.data.replyId +')">\n' +
            '<i class="fas fa-times"></i>\n' +
            '</button>\n'+
            '</div>';
        console.log(content)
        commentList.append(content);
        commentInput.val("");

    }).fail(error=>{
        console.log(error, "reply api error");
    });
}

function deleteComment(replyId) {

    console.log("deleteComment_"+replyId)

    $.ajax({
        url: "/api/reply/" + replyId,
        type: "delete",
        dataType: "json"
    }).done(res=>{
        console.log(res.data)
        if(res.data === 1){
            $("#board-modal-item-comment-"+replyId).remove()
            console.log("?????? ?????? ??????!")
        }else{
            alert("????????? ????????? ????????? ???????????????")
        }
    }).fail(error=>{
        console.log(error, "reply api error")
    })
}

function modalClose() {
    $(".modal-subscribe").css("display", "none");
    location.reload();
}