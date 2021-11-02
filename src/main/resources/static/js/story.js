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
	let likeCheckNum = data.likeCheck;
	let content = data.content;
	let likeCount = data.likeCount;
	let boardUsername = data.userDTO.name;
	let boardUserUsername = data.userDTO.username;
	let boardUserProfileImage = data.userDTO.profileImage;

	let reply = "";

	data.replyList.forEach((data)=> {
		reply +=
			'<div class="sl__item__contents__comment" id="storyCommentItem-'+ data.replyId +'">\n' +
			'<p>\n' +
			'<a href="/user/' + data.userId + '"><b>'+ data.username +'</b></a> ' + data.content + '\n' +
			'</p>\n' +
			'<button type="button" onclick="deleteComment('+ data.replyId +')">\n' +
			'<i class="fas fa-times"></i>\n' +
			'</button>\n' +
			'</div>\n';
	})

	let likeCheck;
	if(likeCheckNum === 1) {
		likeCheck = '<div class="sl__item__contents__icon">\n' +
			'<button>\n' +
			'<i class="fas fa-heart active" id="storyLikeIcon-' + boardId + '" onclick="toggleLike(' + boardId + ')"></i>\n' +
			'</button>\n' +
			'</div>\n';
	}else{
		likeCheck = '<div class="sl__item__contents__icon">\n' +
			'<button>\n' +
			'<i class="far fa-heart" id="storyLikeIcon-' + boardId + '" onclick="toggleLike(' + boardId + ')"></i>\n' +
			'</button>\n' +
			'</div>\n';
	}

	let list = '<div class="story-list__item">\n' +
		'<div class="sl__item__header">\n' +
		'<div>\n' +
		'<a href="/user/'+ data.userDTO.id +'">'+
		'<img class="profile-image" src="/api/image/?username='+ boardUserUsername + "&fileName=" + boardUserProfileImage +'"/>\n' +
		'</a>' +
		'</div>\n' +
		'<a href="/user/' + data.userDTO.id + '">'+ boardUserUsername +'</a>\n'+
		'</div>\n' +
		'<div class="sl__item__img">\n' +
		'<img src="/api/board/image/?username='+ boardUserUsername + "&fileName=" + image + "&boardId=" + boardId +'" />\n' +
		'</div>\n' +
		'<div class="sl__item__contents">\n' +

		likeCheck+

		'<span class="like"><b id="storyLikeCount-'+ boardId +'">좋아요 '+ likeCount +'개 </b></span>\n' +
		'<div class="sl__item__contents__content">\n' +
		'<a href="/user/' + data.userDTO.id + '">'+ '<b>' + boardUserUsername + '</b>' +'</a> '+ content + '\n' +
		'</div>' +
		'<div id="storyCommentList-'+ boardId +'">\n' +
		reply+
		'</div>'+
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

	console.log(scroll)

	if(scroll < -499 && scroll > -501){
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(boardId) {
	let likeIcon = $("#storyLikeIcon-" + boardId);
	let likeCount = $("#storyLikeCount-" + boardId);

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

// (4) 댓글쓰기
function addComment(boardId) {

	let commentInput = $("#storyCommentInput-" + boardId);
	let commentList = $("#storyCommentList-" + boardId);

	let data = {
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	console.log(data)

	$.ajax({
		url: "/api/reply/board/" + boardId,
		type: "post",
		data: data,
		dataType: "json"
	}).done(res=>{
		console.log(res);
		let content = '<div class="sl__item__contents__comment" id="storyCommentItem-' + res.data.replyId + '"> \n' +
			'<p>\n' +
			'<b>'+ res.data.username +' </b>\n' +
			res.data.content +
			'</p>\n' +
			'<button><i class="fas fa-times" onclick="deleteComment('+res.data.replyId+')"></i></button>\n' +
			'</div>';
		console.log(content)
		commentList.prepend(content);
		commentInput.val("");

	}).fail(error=>{
		console.log(error, "reply api error");
	});
}

// (5) 댓글 삭제
function deleteComment(replyId) {

	console.log("deleteComment_"+replyId)

	$.ajax({
		url: "/api/reply/" + replyId,
		type: "delete",
		dataType: "json"
	}).done(res=>{
		console.log(res.data)
		if(res.data === 1){
			$("#storyCommentItem-"+replyId).empty()
			console.log("댓글 삭제 성공!")
		}else{
			console.log("자신의 댓글만 삭제가 가능합니다")
		}
	}).fail(error=>{
		console.log(error, "reply api error")
	})
}







