/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
  (8) 구독자 정보 모달 닫기
 */

let page = 0;


// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(obj, fromUserId, toUserId) {

	if ($(obj).text() === "언팔로우") {

		$.ajax({
			url: "/api/follow/" + toUserId,
			type: "delete",
			data: JSON.stringify({fromUserId : fromUserId}),
			contentType: "application/json",
			dataType: "json"
		}).done(res=>{
			console.log(res)
		}).fail(error=>{
			console.log("구독 api 오류", error)
		});

		$(obj).text("팔로우");
		$(obj).toggleClass("blue");
	} else {
		$.ajax({
			url: "/api/follow/" + toUserId,
			type: "post",
			data: JSON.stringify({fromUserId : fromUserId}),
			contentType: "application/json",
			dataType: "json"
		}).done(res=>{
			console.log(res)
		}).fail(error=>{
			console.log("구독 api 오류", error)
		});
		$(obj).text("언팔로우");
		$(obj).toggleClass("blue");
	}
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId, userId) {
	$(".modal-subscribe").css("display", "flex");
	$("body").css("overflow", "hidden")
	$(".header").css("overflow", "hidden")

	followLoad(pageUserId, userId, page);

	$(".subscribe-list").scroll(() => {

		let scroll = $(".subscribe-list__ul").height() - $(".subscribe-list").scrollTop()

		console.log(scroll)

		if(scroll < 450 && scroll > 448){
			page++;
			console.log(page)
			followLoad(pageUserId, userId, page);
		}
	});
}

function followLoad(pageUserId, userId){
	console.log("followLoad api 호출")
	$.ajax({
		url: "/api/subscribes/" + pageUserId + "/" + userId + "?page=" + page,
		type: "get"
	}).done(res=>{
		console.log(res)
		res.data.forEach((u)=>{
			$(".subscribe-list__ul").append(getSubscribeModalItem(u, userId));
		})
	}).fail(error=>{
		console.log(error, '구독리스트 api 에러')
	});
}

function getSubscribeModalItem(obj, userId) {
	console.log("Subscribe List Console : " + obj.name);

	let state = obj.followCheck;

	console.log("followCheck : " + state);

	let followBtn =
		'<div class="subscribe__item" id="subscribeModalItem-'+ obj.id +'">' +
		'<div class="subscribe__img">' +
		'<img src="/api/image/?username=' + obj.username + '&fileName=' + obj.profileImage +'">' +
		'</div>' +
		'<div class="subscribe__text">' +
		'<div class="sl__item__header"><a href="/user/'+ obj.id +'"><h2>'+ obj.username + '</h2></a></div>' + '<h3>'+ obj.name + '</h3>' +
		'</div>' +
		'<div class="subscribe__btn">' +
		'<button class="cta blue" onclick="toggleSubscribeModal(this, ' + obj.id + ',' + userId +')">언팔로우</button>' +
		'</div>' +
		'</div>';

	let unfollowBtn =
		'<div class="subscribe__item" id="subscribeModalItem-'+ obj.id +'">' +
		'<div class="subscribe__img">' +
		'<img src="/api/image/?username=' + obj.username + '&fileName=' + obj.profileImage +'">' +
		'</div>' +
		'<div class="subscribe__text">' +
		'<div class="sl__item__header"><a href="/user/'+ obj.id +'"><h2>'+ obj.username + '</h2></a></div>' + '<h3>'+ obj.name + '</h3>' +
		'</div>' +
		'<div class="subscribe__btn">' +
		'<button class="cta blue" onclick="toggleSubscribeModal(this, ' + obj.id + ',' + userId +')">팔로우</button>' +
		'</div>' +
		'</div>';

		let nonBtn =
		'<div class="subscribe__item" id="subscribeModalItem-'+ obj.id +'">' +
		'<div class="subscribe__img">' +
		'<img src="/api/image/?username=' + obj.username + '&fileName=' + obj.profileImage +'">' +
		'</div>' +
		'<div class="subscribe__text">' +
		'<div class="sl__item__header"><a href="/user/'+ obj.id +'"><h2>'+ obj.username + '</h2></a></div>' + '<h3>'+ obj.name + '</h3>' +
		'</div>' +
		'<div class="subscribe__btn">' +
		'</div>';

	if(state === 1){
		return followBtn;
	}else if(state === 0){
		return unfollowBtn;
	}else{
		return nonBtn;
	}
}


// (3) 구독자 정보 모달에서 구독하기, 구독취소
function toggleSubscribeModal(obj, toUserid, fromUserId) {
	if ($(obj).text() === "언팔로우") {
		$.ajax({
			url: "/api/follow/" + toUserid,
			type: "delete",
			data: JSON.stringify({fromUserId : fromUserId}),
			contentType: "application/json",
			dataType: "json"
		}).done(res=>{
			console.log(res.data)
		}).fail(err=>{
			console.log("팔로우 모달창 에러", err)
		});
		$(obj).text("팔로우");
		$(obj).toggleClass("blue");
	} else {
		$.ajax({
			url: "/api/follow/" + toUserid,
			type: "post",
			data: JSON.stringify({fromUserId : fromUserId}),
			contentType: "application/json",
			dataType: "json"
		}).done(res=>{
			console.log(res.data)
		}).fail(err=>{
			console.log("팔로우 모달창 에러", err)
		});
		$(obj).text("언팔로우");
		$(obj).toggleClass("blue");
	}
}

// (4) 유저 프로파일 사진 변경 (완)
// 심각한 오류 찾음_ 페이지 리로드 하지 않고 프로필 사진 변경 시 +1 씩 ajax 호출_ ex)첫번째 1번 호출, 두번째 2번 호출, ...
function profileImageUpload(id) {

	$("#userProfileImageInput").click();

	console.log("profileImageUserId : " + id);

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}

		let userProfileImageForm = $('#userProfileImageForm')[0]

		console.log(userProfileImageForm)

		let formData = new FormData(userProfileImageForm)

		console.log("formData",	formData)


		$.ajax({
			processData: false,
			contentType: false,
			url: "/api/userImage/" + id,
			type: "put",
			data: formData,
			enctype: "multipart/form-data",
			dataType: "json"
		}).done(res=>{
			console.log("유저 이미지 ajax 성공 | " + res.data)
			location.reload();
		}).fail(error=>{
			console.log("유저 이미지 ajax 실패 | " + error)
		});
	});
}

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
		let username = res.data.userDTO.username
		let profileImage = res.data.userDTO.profileImage
		$(".p-board-user-username").prepend(res.data.userDTO.username)
		$(".p-board-user-img").prepend('<img class="profile-image" src="/api/image/?username='+ username +'&fileName='+ profileImage +'" onerror="/images/non.jpg">')
	}).fail(error=>{
		console.log(error);
	})
	$(".p-board-user-img").prepend()
}


// (5) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}


// (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

// (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

// (8) 구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}