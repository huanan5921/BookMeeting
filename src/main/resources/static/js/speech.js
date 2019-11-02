/**
 * Created by wen2.lin on 2019/10/31.
 */
var soundContext = undefined;
try {
    var AudioContext = window.AudioContext // our preferred impl
        || window.webkitAudioContext       // fallback, mostly when on Safari
        || false;                          // could not find.

    if (AudioContext) {
        soundContext = new AudioContext();
    } else {
        alert("Audio context not supported");
    }
}catch (e) {
    window.console.log("no sound context found, no audio output. " + e);
}

var key = '1d73b4a679b9498e9aa27ceaa6c41d29';
var region = 'southeastasia'
var SpeechSDK;
var reco;
var language = '';
var voice = '';
var tokenTime = 0;
var token;

    function change() {

        // If an audio file was specified, use it. Else use the microphone.
        // Depending on browser security settings, the user may be prompted to allow microphone use. Using continuous recognition allows multiple
        // phrases to be recognized from a single use authorization.
        var audioConfig = SpeechSDK.AudioConfig.fromDefaultMicrophoneInput();

        var speechConfig = SpeechSDK.SpeechConfig.fromSubscription(key, region);


        speechConfig.speechRecognitionLanguage = 'zh-CN';
        reco = new SpeechSDK.SpeechRecognizer(speechConfig, audioConfig);


        // Before beginning speech recognition, setup the callbacks to be invoked when an event occurs.

        // The event recognizing signals that an intermediate recognition result is received.
        // You will receive one or more recognizing events as a speech phrase is recognized, with each containing
        // more recognized speech. The event will contain the text for the recognition since the last phrase was recognized.
        reco.recognizing = function (s, e) {
            // window.console.log(e);
            var text = e.result.text ;
            $("#phraseDiv").val(text);
        };

        // The event signals that the service has stopped processing speech.
        // https://docs.microsoft.com/javascript/api/microsoft-cognitiveservices-speech-sdk/speechrecognitioncanceledeventargs?view=azure-node-latest
        // This can happen for two broad classes or reasons.
        // 1. An error is encountered.
        //    In this case the .errorDetails property will contain a textual representation of the error.
        // 2. No additional audio is available.
        //    Caused by the input stream being closed or reaching the end of an audio file.
        reco.canceled = function (s, e) {
            // window.console.log(e);
            text += "(cancel) Reason: " + SpeechSDK.CancellationReason[e.reason];
            if (e.reason === SpeechSDK.CancellationReason.Error) {
                text += ": " + e.errorDetails;
            }
            text += "\r\n";
            $("#phraseDiv").val(text);
        };

        // The event recognized signals that a final recognition result is received.
        // This is the final event that a phrase has been recognized.
        // For continuous recognition, you will get one recognized event for each phrase recognized.
        reco.recognized = function (s, e) {
            // window.console.log(e);
            // Indicates that recognizable speech was not detected, and that recognition is done.
            if (e.result.reason === SpeechSDK.ResultReason.NoMatch) {
                var noMatchDetail = SpeechSDK.NoMatchDetails.fromResult(e.result);
                text += "(recognized)  Reason: " + SpeechSDK.ResultReason[e.result.reason] + " NoMatchReason: " + SpeechSDK.NoMatchReason[noMatchDetail.reason] + "\r\n";
            } else {
                text += "(recognized)  Reason: " + SpeechSDK.ResultReason[e.result.reason] + " Text: " + e.result.text + "\r\n";
            }
        };

        // Signals that a new session has started with the speech service
        reco.sessionStarted = function (s, e) {
            // window.console.log(e);
            var text = e.sessionId;
            $("#phraseDiv").val("started");
        };

        // Signals the end of a session with the speech service.
        reco.sessionStopped = function (s, e) {
            // window.console.log(e);
            var text = "(sessionStopped) SessionId: " + e.sessionId + "\r\n";
        };

        // Signals that the speech service has started to detect speech.
        reco.speechStartDetected = function (s, e) {
            // window.console.log(e);
            var text = "(speechStartDetected) SessionId: " + e.sessionId + "\r\n";
            $("#phraseDiv").val("started");
        };

        // Signals that the speech service has detected that speech has stopped.
        reco.speechEndDetected = function (s, e) {
            // window.console.log(e);
            var text = "(speechEndDetected) SessionId: " + e.sessionId + "\r\n";
        };

        // Note: this is how you can process the result directly
        //       rather then subscribing to the recognized
        //       event
        // The continuation below shows how to get the same data from the final result as you'd get from the
        // events above.
        reco.recognizeOnceAsync(
            function (result) {
                // window.console.log(result);
                var text = '';
                switch (result.reason) {
                    case SpeechSDK.ResultReason.RecognizedSpeech:
                        text += " Text: " + result.text;
                        break;
                    case SpeechSDK.ResultReason.NoMatch:
                        var noMatchDetail = SpeechSDK.NoMatchDetails.fromResult(result);
                        text += " NoMatchReason: " + SpeechSDK.NoMatchReason[noMatchDetail.reason];
                        break;
                    case SpeechSDK.ResultReason.Canceled:
                        var cancelDetails = SpeechSDK.CancellationDetails.fromResult(result);
                        text += " CancellationReason: " + SpeechSDK.CancellationReason[cancelDetails.reason];

                        if (cancelDetails.reason === SpeechSDK.CancellationReason.Error) {
                            text += ": " + cancelDetails.errorDetails;
                        }
                        break;
                }
                var value = result.text ;
                $("#phraseDiv").val(value);
                sendMessage(value);
                $("#phraseDiv").val('');
                $('.div-yuyin').removeClass("speaking");
                stopSpeech();
            },
            function (err) {
                window.console.log(err);

                var value = $("#phraseDiv").val()+ "ERROR: " + err;
                $("#phraseDiv").val(value);
                stopSpeech();
            });


    }

    function stopSpeech() {
    if (reco != undefined){
        reco.close();
    }
        reco = undefined;
    }
function sendMessage(text) {
    createUserText(text);
    request(text);
    //发送后清空输入框
    $(".div-textarea").html("");
    //聊天框默认最底部
    $(".chatBox-content").scrollTop($("#chatBox-content-demo")[0].scrollHeight);

}

function sendHttp(audioText, language, voice) {
    //判断token是否失效
    var currentTime = new Date().getMinutes();
    if (currentTime - tokenTime > 9 || currentTime - tokenTime < 0){
        getTtsToken(key,region);
    }
    ttsConvertion(region, token, audioText, language, voice)
}
function getTtsToken(key, region) {
    var xhr = new XMLHttpRequest();
    var url = "https://"+region+".api.cognitive.microsoft.com/sts/v1.0/issueToken"
    xhr.open('POST', url, false); //async=false,采用同步方式处理
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) { //响应数据接收完毕
            token = xhr.responseText;
//                console.log(token)
            tokenTime = new Date().getMinutes();
        }
    }
    xhr.setRequestHeader("Ocp-Apim-Subscription-Key", key);
    xhr.send();
}
function ttsConvertion(region, token, text, language, voice) {
    var xhr = new XMLHttpRequest();
    var _url = "https://"+region+".tts.speech.microsoft.com/cognitiveservices/v1";
//        var content = "<speak version='1.0' xml:lang='"+language+"'><voice xml:lang='"+language+"' xml:gender='Female' name='Microsoft Server Speech Text to Speech Voice ("+voice+")'>" + text + "</voice></speak>";
    var content = "<speak version='1.0' xml:lang='"+language+"'><voice xml:gender='Female' name='Microsoft Server Speech Text to Speech Voice ("+voice+")'>" + text + "</voice></speak>";

//        console.log(_url);
//        console.log(content);
    xhr.open('POST', _url, true); //async=false,采用同步方式处理
    xhr.setRequestHeader("content-type", "application/ssml+xml");
    xhr.setRequestHeader("Authorization", "Bearer " + token);
    xhr.setRequestHeader("X-Microsoft-OutputFormat", "riff-24khz-16bit-mono-pcm");
    xhr.setRequestHeader("User-Agent", "justName");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.responseType = "arraybuffer";
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) { //响应数据接收完毕
            var audioData = xhr.response;
            if (audioData.byteLength > 0){
                playAudioContext(xhr.response);
            }else {
                console.log("返回了长度位0的数据");
            }
        }
    }
    xhr.send(content);
}

function playAudioContext(voiceContext) {

    if (soundContext) {

        var source = soundContext.createBufferSource();
        soundContext.decodeAudioData(voiceContext, function (newBuffer) {
            source.buffer = newBuffer;
            source.connect(soundContext.destination);
            source.start(0);
            source.addEventListener('ended', function () {

            })
        });
    }
}