$(document).ready(function(){
    $('#chat-yuyin').on('click',function(){
        $('#chat-wenzi,.div-yuyin').show();
        $('#chat-yuyin,.div-textarea').hide();
        func = true;
    })
    $('#chat-wenzi').on('click',function(){
        stopSpeech();
        $('#chat-wenzi,.div-yuyin').hide();
        $('#chat-yuyin,.div-textarea').show();
        func = false;
        $('#yuyin-block').hide();
        $('.div-yuyin').removeClass("speaking");
        stopSpeech();
    })
    $('.div-yuyin').on('click',function(){
        if($('#yuyin-block>textarea').val()!=''){
            $('#yuyin-block>textarea').val('');
        }
        $('#yuyin-block').show();
        $('.div-yuyin').addClass("speaking");
    })

})
var func = false;
var seesionId = uuid();
var attrId;

function request(content) {
    if ($.trim(content) == ''){
        return;
    }
    $('#yuyin-block').hide();
    var url = "/bm/bookMeeting";
    var o = {};
    o.seesionId = seesionId;
    o.attrId = attrId;
    o.text = content;
    $.post(url, o, function(result){
        console.log(result);
        if (func == true){
            sendHttp(result.responseText, 'zh-CN', 'zh-CN, HuihuiRUS');
        }
        setMessage(result)
    });
}

function setMessage(data) {
    attrId = data.attrId;
    if (data.type == '1'){
        //普通消息
        createCommonText(data.responseText);
        // createMeetingText(data.data);
    }else if (data.type == '2'){
        //投影仪
        createMediaText(data.responseText);
        // createMeetingText(data.data);
    }else if (data.type == '3'){
        //选择会议室
        createChoiceMeetingText(data.data.meetingRooms)
    }else if (data.type == '4'){
        //预定成功
        createCommonText(data.responseText);
        createMeetingText(data.data);
    }

}

//普通应答消息
function createCommonText(text) {
    var html = '<div class="clearfloat">'+
        '<div class="author-name">' +
        '<small class="chat-date">2017-12-02 14:26:58</small>' +
        '</div>' +
        '<div class="left">' +
        '<div class="chat-avatars"><img src="img/robot.png" alt="robot" /></div>' +
        '<div class="chat-message projector-yes-no">' +
        '<div>' +
        '<ul>' +
        '<li>' +
        '<label>'+text+'</label>' +
        '</li>' +
        '</ul>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    $(".chatBox-content-demo").append(html);
    $(".chatBox-content").scrollTop($("#chatBox-content-demo")[0].scrollHeight);

}

//是否需要投影
function createMediaText(text) {
    var html = '<div class="clearfloat">'+
        '<div class="author-name">' +
        '<small class="chat-date">2017-12-02 14:26:58</small>' +
        '</div>' +
        '<div class="left">' +
        '<div class="chat-avatars"><img src="img/robot.png" alt="robot" /></div>' +
        '<div class="chat-message projector-yes-no">' +
        '<div>' +
        '<ul>' +
        '<li>' +
        '<label>'+text+'</label>' +
        '</li>' +
        '<li>' +
        '<input type="radio" id="projector-yes" name="projector">' +
        '<label for="projector-yes">YES</label>' +
        '</li>' +
        '<li>' +
        '<input type="radio" id="projector-no" name="projector">' +
        '<label for="projector-no">NO</label>' +
        '</li><li><button id="affirm-projector">确定</button></li>' +
        '</ul>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    $(".chatBox-content-demo").append(html);
    $(".chatBox-content").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
    $('#affirm-projector').on('click',function () {
        if($('input[name="projector"]:checked').length == 0){
            alert('请选择是否需要投影');
        }else {
            var checkedId = $('input[name="projector"]:checked').attr('id');
            // alert($('label[for='+checkedId+']').html());
            var textContent = $('label[for='+checkedId+']').html();
            sendMessage(textContent);
        }
    })
}

//选择会议室
function createChoiceMeetingText(meetingRooms) {
    var html = '<div class="clearfloat">' +
        '<div class="author-name">' +
        '<small class="chat-date">2017-12-02 14:26:58</small>' +
        '</div>' +
        '<div class="left">' +
        '<div class="chat-avatars"><img src="img/robot.png" alt="robot" /></div>' +
        '<div class="chat-message projector-yes-no">' +
        '<div>' +
        '<ul>' +
        '<li>' +
        '<label>以下会议室符合您的条件，请选择一个</label>' +
        '</li>';
    for (var i = 0; i < meetingRooms.length; i++) {
        html += '<li>' +
            '<input type="radio" id="meeting-'+i+'" name="meeting-room">' +
            '<label for="meeting-'+i+'">'+meetingRooms[i]+'</label>' +
            '</li>';
    }
    html += '<li><button id="affirm-meeting">确定</button></li>' +
        '</ul>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    $(".chatBox-content-demo").append(html);
    $(".chatBox-content").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
    $('#affirm-meeting').on('click',function(){
        if($('input[name="meeting-room"]:checked').length == 0){
            alert('请选择一间会议室');
        }else{
            var checkedId = $('input[name="meeting-room"]:checked').attr('id');
            // alert($('label[for='+checkedId+']').html());
            var textContent = $('label[for='+checkedId+']').html();
            sendMessage(textContent)
        }
    })
}

//会议卡片
function createMeetingText(data) {
    var html = '<div id="meeting-card">'+
        '<ul>' +
        '<li class="meeting-tit">'+data.meetingTheme+'</li>' +
        '<li class="meeting-time">' +
        '<p>会议时间:</p>' +
        '<p>'+data.meetingDate+': '+data.startTime+'-'+data.endTime+'</p>' +
        '</li>' +
        '<li class="meeting-personnel">' +
        '<p>与会人数:</p>' +
        '<p>'+data.personNum+'</p>' +
        '</li>' +
        '<li class="meeting-site">' +
        '<p>会议地点:</p>' +
        '<p>'+data.address+'</p>' +
        '</li>' +
        '<li class="meeting-site">' +
        '<p>是否需要投影: </p>' +
        '<p>'+data.isMedia+'</p>' +
        '</li>' +
        '</ul>' +
        '</div>';

    $(".chatBox-content-demo").append(html);
    $(".chatBox-content").scrollTop($("#chatBox-content-demo")[0].scrollHeight);
}

//用户消息
function createUserText(textContent) {
    if ($.trim(textContent) == ''){
        return;
    }
    var html = "<div class=\"clearfloat\">" +
        "<div class=\"author-name\"><small class=\"chat-date\">2017-12-02 14:26:58</small> </div> " +
        "<div class=\"right\"> <div class=\"chat-message\"> " + textContent + " </div> " +
        "<div class=\"chat-avatars\"><img src=\"img/icon01.png\" alt=\"头像\" /></div> </div> </div>"
    $(".chatBox-content-demo").append(html);
}

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}