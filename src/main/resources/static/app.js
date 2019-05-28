var stompClient = null;
var sessionId = "";
var url = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#room_form").show();

    }
    else {
        $("#chat_section").hide();
        $("#room_form").hide();
        $("#message_form").hide();
    }
    $("#conversation").html("");
}

function connect() {
    var socket = new SockJS('/secured/room');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        url = stompClient.ws._transport.url;
        url = url.replace(
            "ws://localhost:8080/secured/room", "");
        url = url.replace("/websocket", "");
        url = url.replace(/^[0-9]+\//, "");
        console.log("Your current session is: " + url);
        sessionId = url;
        setConnected(true);
        stompClient.subscribe('/secured/user/queue/specific-room'
            + '-user' + sessionId, function (greeting) {
                processMessage(JSON.parse(greeting.body))
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.send("/app/secured/user/disconnect" + url, {}, {});
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function createRoom() {
    stompClient.send("/app/secured/user/connect" + url, {}, JSON.stringify(
        {'userName': $("#user_name").val(),
        'roomName': $("#room_name").val(),
        'userLanguage': $("#user_lang").val(),
        'request': 1}
    ));
    /*
    $("logo").hide();
    $("#room_form").hide();
    $("#message_form").show();
    $("#section_title").show();
    $("#conversation").show();
    */
}

function joinRoom() {
    stompClient.send("/app/secured/user/connect" + url, {}, JSON.stringify(
        {'userName': $("#join_user_name").val(),
        'roomName': $("#join_room_name").val(),
        'userLanguage': $("#join_user_lang").val(),
        'request': 2}
    ));
    /*
    $("logo").hide();
    $("#room_form").hide();
    $("#message_form").show();
    $("#section_title").show();
    $("#conversation").show();
    */
}

function sendMessage() {
    stompClient.send("/app/secured/user/send" + url, {}, JSON.stringify(
        {'content': $("#room_msg").val()}
    ));
}

function processMessage(message) {
    if (message.systemFlag) {
        if (message.infoCode == 10 || message.infoCode == 20) {
            $("#room_form").hide();
            $("#message_form").show();
            $("#chat_section").show();

        }

    }
    $("#chat_section").show();
    showMessage(message);
    console.log(message);
}

function showMessage(message) {
    var today = new Date();
    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    if (message.systemFlag==false){
        if (message.toReceiver==true){
            $("#conversation").append("<li class=\"left\"><span class=\"d-inline-block chat-img\"><img src=\"http://placehold.it/50/55C1E7/fff&text=U\" alt=\"User Avatar\" class=\"img-circle\" /></span><span class=\"d-inline-block chat-body\"><div class=\"header\"><strong class=\"primary-font\">"+message.sender+"</strong> <small class=\"pull-right text-muted\"><span class=\"glyphicon glyphicon-time\"></span>"+time+"</small ></div><p>" + message.content + "</p></div></li>");
        }
        else{
          $("#conversation").append("<li class=\"left\"><span class=\"d-inline-block chat-img\"><img src=\"http://placehold.it/50/FA6F57/fff&text=ME\" alt=\"User Avatar\" class=\"img-circle\" /></span><span class=\"d-inline-block chat-body\"><div class=\"header\"><strong class=\"primary-font\">"+message.sender+"</strong> <small class=\"pull-right text-muted\"><span class=\"glyphicon glyphicon-time\"></span>"+time+"</small ></div><p>" + message.content + "</p></div></li>");
        }
    }
    if (message.systemFlag==true){
        $("#conversation").append("<li>"+message.content+"</li>");
    }

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#create_room" ).click(function() { createRoom(); })
    $( "#join_room" ).click(function() { joinRoom(); })
    $( "#send_msg" ).click(function() { sendMessage(); })
    setConnected(false)
});


// <script>
var audio = document.querySelector('audio');

function captureMicrophone(callback) {
    btnReleaseMicrophone.disabled = false;

    if(microphone) {
        callback(microphone);
        return;
    }

    if(typeof navigator.mediaDevices === 'undefined' || !navigator.mediaDevices.getUserMedia) {
        alert('This browser does not supports WebRTC getUserMedia API.');

        if(!!navigator.getUserMedia) {
            alert('This browser seems supporting deprecated getUserMedia API.');
        }
    }

    navigator.mediaDevices.getUserMedia({
        audio: isEdge ? true : {
            echoCancellation: false
        }
    }).then(function(mic) {
        callback(mic);
    }).catch(function(error) {
        alert('Unable to capture your microphone. Please check console logs.');
        console.error(error);
    });
}

function replaceAudio(src) {
    var newAudio = document.createElement('audio');
    newAudio.controls = true;
    newAudio.autoplay = true;

    if(src) {
        newAudio.src = src;
    }

    var parentNode = audio.parentNode;
    parentNode.innerHTML = '';
    parentNode.appendChild(newAudio);

    audio = newAudio;
}

function stopRecordingCallback() {
    replaceAudio(URL.createObjectURL(recorder.getBlob()));

    btnStartRecording.disabled = false;

    setTimeout(function() {
        if(!audio.paused) return;

        setTimeout(function() {
            if(!audio.paused) return;
            audio.play();
        }, 1000);

        audio.play();
    }, 300);

    audio.play();

    btnDownloadRecording.disabled = false;

    if(isSafari) {
        click(btnReleaseMicrophone);
    }
}

var isEdge = navigator.userAgent.indexOf('Edge') !== -1 && (!!navigator.msSaveOrOpenBlob || !!navigator.msSaveBlob);
var isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);

var recorder; // globally accessible
var microphone;

var btnStartRecording = document.getElementById('btn-start-recording');
var btnStopRecording = document.getElementById('btn-stop-recording');
var btnReleaseMicrophone = document.querySelector('#btn-release-microphone');
var btnDownloadRecording = document.getElementById('btn-download-recording');

btnStartRecording.onclick = function() {
    this.disabled = true;
    this.style.border = '';
    this.style.fontSize = '';

    if (!microphone) {
        captureMicrophone(function(mic) {
            microphone = mic;

            if(isSafari) {
                replaceAudio();

                audio.muted = true;
                audio.srcObject = microphone;

                btnStartRecording.disabled = false;
                btnStartRecording.style.border = '1px solid red';
                btnStartRecording.style.fontSize = '150%';

                alert('Please click startRecording button again. First time we tried to access your microphone. Now we will record it.');
                return;
            }

            click($( "#btn-start-recording" ));
        });
        return;
    }

    replaceAudio();

    audio.muted = true;
    audio.srcObject = microphone;

    var options = {
        type: 'audio',
        numberOfAudioChannels: isEdge ? 1 : 2,
        checkForInactiveTracks: true,
        bufferSize: 16384
    };

    if(isSafari || isEdge) {
        options.recorderType = StereoAudioRecorder;
    }

    if(navigator.platform && navigator.platform.toString().toLowerCase().indexOf('win') === -1) {
        options.sampleRate = 48000; // or 44100 or remove this line for default
    }

    if(isSafari) {
        options.sampleRate = 44100;
        options.bufferSize = 4096;
        options.numberOfAudioChannels = 2;
    }

    if(recorder) {
        recorder.destroy();
        recorder = null;
    }

    recorder = RecordRTC(microphone, options);

    recorder.startRecording();

    btnStopRecording.disabled = false;
    btnDownloadRecording.disabled = true;
};

btnStopRecording.onclick = function() {
    this.disabled = true;
    recorder.stopRecording(stopRecordingCallback);
};

btnReleaseMicrophone.onclick = function() {
    this.disabled = true;
    btnStartRecording.disabled = false;

    if(microphone) {
        microphone.stop();
        microphone = null;
    }

    if(recorder) {
        // click(btnStopRecording);
    }
};

btnDownloadRecording.onclick = function() {
    this.disabled = true;
    if(!recorder || !recorder.getBlob()) return;

    if(isSafari) {
        recorder.getDataURL(function(dataURL) {
            SaveToDisk(dataURL, getFileName('mp3'));
        });
        return;
    }

    var blob = recorder.getBlob();
    var file = new File([blob], getFileName('mp3'), {
        type: 'audio/mp3'
    });
    invokeSaveAsDialog(file);
};

function click(el) {
    el.disabled = false; // make sure that element is not disabled
    var evt = document.createEvent('Event');
    evt.initEvent('click', true, true);
    el.dispatchEvent(evt);
}

function getRandomString() {
    if (window.crypto && window.crypto.getRandomValues && navigator.userAgent.indexOf('Safari') === -1) {
        var a = window.crypto.getRandomValues(new Uint32Array(3)),
            token = '';
        for (var i = 0, l = a.length; i < l; i++) {
            token += a[i].toString(36);
        }
        return token;
    } else {
        return (Math.random() * new Date().getTime()).toString(36).replace(/\./g, '');
    }
}

function getFileName(fileExtension) {
    var d = new Date();
    var year = d.getFullYear();
    var month = d.getMonth();
    var date = d.getDate();
    return 'RecordRTC-' + year + month + date + '-' + getRandomString() + '.' + fileExtension;
}

function SaveToDisk(fileURL, fileName) {
    // for non-IE
    if (!window.ActiveXObject) {
        var save = document.createElement('a');
        save.href = fileURL;
        save.download = fileName || 'unknown';
        save.style = 'display:none;opacity:0;color:transparent;';
        (document.body || document.documentElement).appendChild(save);

        if (typeof save.click === 'function') {
            save.click();
        } else {
            save.target = '_blank';
            var event = document.createEvent('Event');
            event.initEvent('click', true, true);
            save.dispatchEvent(event);
        }

        (window.URL || window.webkitURL).revokeObjectURL(save.href);
    }

    // for IE
    else if (!!window.ActiveXObject && document.execCommand) {
        var _window = window.open(fileURL, '_blank');
        _window.document.close();
        _window.document.execCommand('SaveAs', true, fileName || fileURL)
        _window.close();
    }
}

// </script>
