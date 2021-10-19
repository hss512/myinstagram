let ifIdCheck = 0;

function button_twinCheck(){

    let username = $("#userId").val()

    console.log(username)

    $.ajax({
        url: "/api/idCheck",
        type: "post",
        contentType: 'application/json',
        data: JSON.stringify({"username" : username}),
    }).done(res=>{
        ifIdCheck++;
        console.log(ifIdCheck)
        console.log(res);
        if(res.data === 1) {
            alert('중복된 아이디입니다')
            ifIdCheck = 2;
        }else {
            alert('사용 가능한 아이디입니다')
            $("#userId").prop('readonly', true)
        }
    }).fail(error=>{
        console.log(error, "idCheck api 오류")
    });
}

function ifIdCheckSubmit(){
    if(ifIdCheck === 0){
        alert("아이디 중복확인을 클릭하지 않았습니다!")
    }else if(ifIdCheck === 1){
        $("#ifIdCheck").attr("type", "submit");
    }else{
        alert("중복된 아이디입니다")
        ifIdCheck = 0;
    }
}

function idCheck(obj){
    // 아이디 특수문자 제한 로직 짜야 됨!
}