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
