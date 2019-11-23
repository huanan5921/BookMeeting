$(document).ready(function(){
    $('#chat-wenzi').show();
    $('#chat-yuyin').hide();
    $(".div-textarea").attr("contenteditable", false);
    change();
    $('#yuyin-block').hide();
    $('#chat-yuyin').on('click',function(){
        $('#chat-wenzi').show();
        $('#chat-yuyin').hide();
        $(".div-textarea").attr("contenteditable", false);
        setVal(".div-textarea", '')
        func = true;
        change();
    })
    $('#chat-wenzi').on('click',function(){
        stopSpeech();
        $('#chat-wenzi').hide();
        $('#chat-yuyin,.div-textarea').show();
        func = false;

        setVal(".div-textarea", '')
        $(".div-textarea").attr("contenteditable", true);
        // $('.div-yuyin').removeClass("speaking");
        stopSpeech();
    })
    pathName = window.document.location.pathname;
    projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    audio.addEventListener('ended', function () {
        playStatus = false;
    }, false);
})
var playStatus = false;
var func = false;
var seesionId = uuid();
var attrId='';
var pathName;
var projectName;

function request(content) {
    // $('#yuyin-block').hide();

    var url = projectName+"/botProcess";
    var o = {};
    o.seesionId = seesionId;
    o.attrId = attrId;
    o.text = content;
    $.post(url, o, function(result){
        console.log(result);
        if (func == true && result.type != '-1'){
            // sendHttp(result.responseText, 'zh-CN', 'zh-CN, HuihuiRUS');
        }
        setMessage(result)
    });
}
const audio = new Audio();
function setMessage(data) {
    if (data.voiceUrl != null){
        audio.src = projectName+data.voiceUrl;
        audio.play();
        playStatus = true;
    }
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
        createChoiceMeetingText(data.responseText, data.data.meetingRooms)
    }else if (data.type == '4'){
        //预定成功
        createCommonText(data.responseText);
        createMeetingText(data.data);
    }
    if (data.type != '-1'){
        //如果反馈有效，清除定时器，并新建一个定时器
        if (timeInstance != undefined){
            clearTimeout(timeInstance);
        }
        isLive = true;
        // $('.chatBox-send').hide();
        // $('#yuyin-block').show();
        timeInstance = window.setTimeout(function () {
            var url = projectName+"/clearSeesion";
            var o = {};
            o.seesionId = seesionId;
            $.post(url, o, function(result){
                if (result == '1'){
                    createCommonText("由于您长时间未回复，我们将终结这次会话，需要唤醒我请叫\"小智同学\"");
                    isLive = false;
                }
            });

        },30000);
    }

}
var timeInstance = undefined;
var isLive = false;

//普通应答消息
function createCommonText(text) {
    var html = '<div class="clearfloat">'+
        '<div class="author-name">' +
        '<small class="chat-date">'+getCurrentTime()+'</small>' +
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
        '<small class="chat-date">'+getCurrentTime()+'</small>' +
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
function createChoiceMeetingText(text, meetingRooms) {
    var html = '<div class="clearfloat">' +
        '<div class="author-name">' +
        '<small class="chat-date">'+getCurrentTime()+'</small>' +
        '</div>' +
        '<div class="left">' +
        '<div class="chat-avatars"><img src="img/robot.png" alt="robot" /></div>' +
        '<div class="chat-message projector-yes-no">' +
        '<div>' +
        '<ul>' +
        '<li>' +
        '<label>'+text+'</label>' +
        '</li>';
    for (var i = 0; i < meetingRooms.length; i++) {
        html += '<li>' +
            '<input type="radio" id="meeting-'+i+'" name="meeting-room">' +
            '<label id="'+meetingRooms[i].fdID+'" for="meeting-'+i+'">'+meetingRooms[i].fdName+'</label>' +
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
            var roomId = $('label[for='+checkedId+']').attr("id");
            sendMessage(textContent)
        }
    })
}

//会议卡片
function createMeetingText(data) {
    var html = '<div id="meeting-card">'+
        '<ul>' +
        '<li class="meeting-tit">'+data.data.meetingTheme.attrName+'</li>' +
        '<li class="meeting-time">' +
        '<p>会议时间:</p>' +
        '<p>'+data.data.meetingDate.attrName+': '+data.data.startTime.attrName+'-'+data.data.endTime.attrName+'</p>' +
        '</li>' +
        // '<li class="meeting-personnel">' +
        // '<p>与会人数:</p>' +
        // '<p>'+data.personNum+'</p>' +
        // '</li>' +
        '<li class="meeting-site">' +
        '<p>会议地点:</p>' +
        '<p>'+data.data.address.attrName+'</p>' +
        '</li>' +
        // '<li class="meeting-site">' +
        // '<p>是否需要投影: </p>' +
        // '<p>'+data.isMedia+'</p>' +
        // '</li>' +
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
        "<div class=\"author-name\"><small class=\"chat-date\">"+getCurrentTime()+"</small> </div> " +
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

function getCurrentTime(){     	//获取时间
    var date=new Date();

    var year=date.getFullYear();
    var month=date.getMonth();
    var day=date.getDate();

    var hour=date.getHours();
    var minute=date.getMinutes();
    var second=date.getSeconds();

    //这样写显示时间在1~9会挤占空间；所以要在1~9的数字前补零;
    if (hour<10) {
        hour='0'+hour;
    }
    if (minute<10) {
        minute='0'+minute;
    }
    if (second<10) {
        second='0'+second;
    }


    var x=date.getDay();//获取星期


    var time=year+'/'+month+'/'+day+'/'+hour+':'+minute+':'+second
    return time;
}
