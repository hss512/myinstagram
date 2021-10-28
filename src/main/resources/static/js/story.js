/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

let page = 0;

// (1) 스토리 로드하기
function storyLoad() {
	$.ajax({
		url: "api/board?page=" + page,
		dataType: "json"
	}).done(res=>{
		/*console.log(res.data)*/
		res.data.content.forEach((data)=>{
			console.log(data)
			$("#storyList").append(getStoryItem(data));
		})
	}).fail(error=>{
		console.log(error)
	});
}

storyLoad();

function getStoryItem(data) {

	let boardId = data.id;
	let image = data.imageUrl;
	let content = data.content;
	let likeCount = data.likeCount;
	let boardUserId = data.userDTO.id;
	let boardUsername = data.userDTO.name;
	let boardUserUsername = data.userDTO.username;
	let boardUserProfileImage = data.userDTO.profileImage;

	let likeCheck = '<div class="sl__item__contents__icon">\n' +
		'<button>\n' +
		'<i class="fas fa-heart active" id="storyLikeIcon-'+ boardId +'" onclick="toggleLike('+ boardId +')"></i>\n' +
		'</button>\n' +
		'</div>\n';

	let list = '<div class="story-list__item">\n' +
		'<div class="sl__item__header">\n' +
		'<div>\n' +
		'<img class="profile-image" src="/api/image/?username='+ boardUserUsername + "&fileName=" + boardUserProfileImage +'"/>\n' +
		'</div>\n' +
		'<div>' + boardUsername +'</div>\n' +
		'</div>\n' +
		'<div class="sl__item__img">\n' +
		'<img src="/api/board/image/?username='+ boardUserUsername + "&fileName=" + image + "&boardId=" + boardId +'" />\n' +
		'</div>\n' +
		'<div class="sl__item__contents">\n' +



		'<span class="like"><b id="storyLikeCount-'+ boardId +'">3 </b>likes</span>\n' +
		'<div class="sl__item__contents__content">\n' +
		'<p>' + content + '</p>\n' +
		'</div>\n' +
		'<div id="storyCommentList-'+ boardId +'">\n' +
		'<div class="sl__item__contents__comment" id="storyCommentItem-'+ boardId +'">\n' +
		'<p>\n' +
		'<b>Lovely :</b> 부럽습니다.\n' +
		'</p>\n' +
		'<button type="button" onclick="deleteComment('+ boardId +')">\n' +
		'<i class="fas fa-times"></i>\n' +
		'</button>\n' +
		'</div>\n' +
		'</div>\n' +
		'<div class="sl__item__input">\n' +
		'<input type="text" placeholder="댓글 달기..." id="storyCommentInput-'+ boardId +'" />\n' +
		'<button type="button" onClick="addComment('+ boardId +')">게시</button>\n' +
		'</div>\n' +
		'</div>\n' +
		'</div>'

	return list;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {

	let scroll = $(window).scrollTop() - ($(document).height() - $(window).height())

	if(scroll < 1 && scroll > -1){
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(boardId) {
	let likeIcon = $("#storyLikeIcon-" + boardId);
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
			console.log(res, "좋아요 취소 api 성공")
		}).fail(error=>{
			console.log(error, "좋아요 취소 api 실패")
		});
	}
}

// (4) 댓글쓰기
function addComment(boardId) {

	let commentInput = $("#storyCommentInput-1");
	let commentList = $("#storyCommentList-1");

	let data = {
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-2""> 
			    <p>
			      <b>GilDong :</b>
			      댓글 샘플입니다.
			    </p>
			    <button><i class="fas fa-times"></i></button>
			  </div>
	`;
	commentList.prepend(content);
	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment(boardId) {
	console.log("deleteComment_"+boardId)
}







