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
        page++;
    }).fail(error=>{
        console.log(error, "getAllBoardList Api error");
    });
}

getAllBoardList();

// 게시물 모달창
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
        let likeIcon = $("#modalLikeIcon").attr("id", "modalLikeIcon-" + res.data.id)
        $(".modal-heart-icon").attr("onclick", "toggleLike(" + res.data.id + ")")
        let username = res.data.userDTO.username;
        let profileImage = res.data.userDTO.profileImage;
        $(".p-board-user-username").prepend(res.data.userDTO.username)
        $(".p-board-user-img").prepend('<img class="profile-image" src="/api/image/?username='+ username +'&fileName='+ profileImage +'" onerror="/images/non.jpg">')
        $(".like").prepend("<b id='modalLikeCount'>좋아요 " + res.data.likeCount + "개</b>")
        $(".betweenTime-time").prepend(res.data.time)
        if(res.data.likeCheck === 1){
            likeIcon.addClass("fas");
            likeIcon.addClass("active");
            likeIcon.removeClass("far");
        }

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
            likeCount.empty().prepend("좋아요 " + res.data + "개");
            console.log(res, "좋아요 api 성공")
        }).fail(error=>{
            console.log(error, "좋아요 api 실패")
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
            likeCount.empty().prepend("좋아요 " + res.data + "개");
            console.log(res, "좋아요 취소 api 성공")
        }).fail(error=>{
            console.log(error, "좋아요 취소 api 실패")
        });
    }
}

function modalClose() {
    $(".modal-subscribe").css("display", "none");
    location.reload();
}