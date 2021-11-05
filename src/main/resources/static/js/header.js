$(".search-input").bind("change keyup paste", function (){

    let input = $(".search-input");
    let text = input.val()
    input.attr("value", text)

    if(text !== oldText && text !== ""){
        console.log(text)
        searchAjax(text);
    }

    oldText = text;

})

let oldText;

function searchAjax(text){
    $.ajax({
        url: "api/search/"+ text,
        dataType: "json"
    }).done(res=>{
        $(".search").append(

        )
        if (res.data === null){
            console.log(res.message)
        }else{
            console.log(res.data)
        }
    }).fail(error=>{
        console.log(error)
    })
}