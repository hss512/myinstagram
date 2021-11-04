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
	let boardUserUsername = data.userDTO.username;
	let boardUserProfileImage = data.userDTO.profileImage;

	let reply = "";

	if(data.replyList.length < 2){
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
	}else{
		reply += '<div class="sl__item__contents__comment" id="moreComment">'+
		'<a href="javascript:void(0)" id="href_direct_'+ boardId +'" onclick="boardModalOpen('+ boardId +')">'+
		'<span>댓글 '+ data.replyList.length + '개 모두 보기</span>'+
		'</a>'+
		'</div>';
	}

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
		'<div class="sl__item__img" id="sl__item__img-'+ data.id +'">\n' +
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

function boardModalOpen(boardId, userId) {

	$(".modal-board").css("display", "flex");
	$("body").css("overflow", "hidden")
	$(".header").css("overflow", "hidden")
	$(".board-img").prepend($("#sl__item__img-" + boardId).children()[0])

	$.ajax({
		url: "/p/board/" + boardId,
		type: "get",
		dataType: "json"
	}).done(res=>{
		console.log(res.data);
		$("#boardId-").attr("id", "boardId-" + res.data.id);
		let likeIcon = $("#modalLikeIcon").attr("id", "modalLikeIcon-" + res.data.id)
		$(".modal-heart-icon").attr("onclick", "modalToggleLike(" + res.data.id + ")")
		let username = res.data.userDTO.username;
		let profileImage = res.data.userDTO.profileImage;
		$("#boardUserUsername").prepend(res.data.userDTO.username)
		$("#boardUserImg").prepend('<img class="profile-image" src="/api/image/?username='+ username +'&fileName='+ profileImage +'" onerror="/images/non.jpg">')
		$(".modal-like").prepend("<b id='modalLikeCount'>좋아요 " + res.data.likeCount + "개</b>")
		$(".betweenTime-time").prepend(res.data.time)
		$(".sl__item__input__modal").append('<input type="text" placeholder="댓글 달기..." id="storyModalCommentInput-'+ res.data.id +'" />\n'+'<button type="button" onClick="addModalComment('+ res.data.id +')">게시</button>');
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
				'<button type="button" onclick="deleteModalComment('+ obj.replyId +')">\n' +
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

function addModalComment(boardId) {

	let commentInput = $("#storyModalCommentInput-" + boardId);
	let commentList = $(".board-modal-list__ul");

	let data = {
		content: commentInput.val()
	}

	console.log(data)

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
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
			'<button type="button" onclick="deleteModalComment('+ res.data.replyId +')">\n' +
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
			alert("자신의 댓글만 삭제가 가능합니다")
		}
	}).fail(error=>{
		console.log(error, "reply api error")
	})
}

function deleteModalComment(replyId) {

	console.log("deleteComment_"+replyId)

	$.ajax({
		url: "/api/reply/" + replyId,
		type: "delete",
		dataType: "json"
	}).done(res=>{
		console.log(res.data)
		if(res.data === 1){
			$("#board-modal-item-comment-"+replyId).remove()
			console.log("댓글 삭제 성공!")
		}else{
			alert("자신의 댓글만 삭제가 가능합니다")
		}
	}).fail(error=>{
		console.log(error, "reply api error")
	})
}

function modalToggleLike(boardId) {
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
	$("body").css("overflow", '')
	/*$(".header").css("overflow", '')
	$(".modal-board").css("display", "none");*/
	document.location.reload(true);

}