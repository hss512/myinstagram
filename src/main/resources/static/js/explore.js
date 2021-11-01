let page = 0;

function getAllBoardList(){
    $.ajax({
        url: "/api/board/all?page=" + page,
        type: "get",
        dataType: "json"
    }).done(res=>{
        console.log(res.data.content)
        res.data.content.forEach(data=>{
            let list = '<div class="img-box">\n' +
                '<a href="/user/' + data.userDTO.id + '"> <img src="/api/board/image/?username='+ data.userDTO.username + "&fileName=" + data.imageUrl + "&boardId=" + data.id +'" />\n' +
                '</a>\n' +
                '<div class="comment" onclick="location.href='+ "'/p/board/" + data.id + "'" +'">' +
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
