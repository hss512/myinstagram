// (1) 회원정보 수정
function update(id, event) {

    event.preventDefault();

    let data = $('#profileUpdate').serialize();

    console.log(id)
    console.log(data)

    $.ajax({
        url: "/api/user/"+id,
        method: "put",
        dataType: "json",
        data: data,
    }).done(res=>{
        console.log(res.data)
        location.href = '/user/update';
    }).fail(error=>{
        console.log(error, "오류")
    });

}