let oldText;

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

$(".search-input").click(function (){
    $(".search").append('<div class="noSearch" role="dialog"></div>\n' +
        '<div class="searchResult">\n' +
        '<div aria-hidden="false" class="searchResult-result">\n' +
        '<div class="searchResult-resultBox">\n' +
        '<div class="searchResult-resultBox-list">\n' +
        '<ul class="searchResult-resultBox-list-ul"></ul>'+
        '</div>\n' +
        '</div>\n' +
        '</div>\n' +
        '</div>')
})

$('html').click(function (e){
    if($(e.target).hasClass("noSearch")){
        $(".noSearch").remove()
        $(".searchResult").remove()
    }
})

function searchAjax(text){
    $.ajax({
        url: "/api/search/"+ text,
        dataType: "json"
    }).done(res=>{
        $(".searchResult-item").parent("div").remove();
        if (res.data === null){
            console.log(res.message)
        }else{
            res.data.forEach((data)=>{
                let href = data.id
                let username = data.username
                let name = data.name
                let profileImage = data.profileImage

                $(".searchResult-resultBox-list-ul").append(
                    '<div role="none">'+
                    '<div class="searchResult-item">\n' +
                    '<a class="searchHref" href="/user/' + href +'">\n' +
                    '<div class="searchResult-items">\n'+
                    '<div class="searchResult-item-img">\n' +
                    '<img src="/api/image/?username='+ username +'&fileName='+ profileImage +'" alt="/images/none"/>\n' +
                    '</div>\n' +
                    '<div class="searchResult-item-info">\n' +
                    '<div class="searchResult-item-username">\n' +
                    '<span>'+ username +'</span>\n' +
                    '</div>\n' +
                    '<div class="searchResult-item-name">\n' +
                    name+
                    '</div>\n' +
                    '</div>\n' +
                    '</div>'+
                    '</a>\n'+
                    '</div>'+
                    '</div>'
                )
                $(document).ready(function (){
                    let cursorEvent = $('.searchResult-item')
                    cursorEvent.mouseenter(function (){
                        $(this).parent("div").addClass('backgroundChange')
                    })
                })
                $(document).ready(function (){
                    let cursorEvent = $('.searchResult-item')
                    cursorEvent.mouseleave(function (){
                        $(this).parent("div").removeClass('backgroundChange')
                    })
                })
            })
        }
    }).fail(error=>{
        console.log(error)
    })
}

