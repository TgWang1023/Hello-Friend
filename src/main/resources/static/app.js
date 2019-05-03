var stompClient = null;
var sessionId = "";
var url = null

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#room_form").show();

    }
    else {
        $("#section_title").hide();
        $("#conversation").hide();
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
//                showMessage(JSON.parse(greeting.body).content);
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
    $("logo").hide();
    $("#room_form").hide();
    $("#message_form").show();
    $("#section_title").show();
    $("#conversation").show();

}

function joinRoom() {
    stompClient.send("/app/secured/user/connect" + url, {}, JSON.stringify(
        {'userName': $("#join_user_name").val(),
        'roomName': $("#join_room_name").val(),
        'userLanguage': $("#join_user_lang").val(),
        'request': 2}
    ));
    $("logo").hide();
    $("#room_form").hide();

    $("#message_form").show();
    $("#section_title").show();
    $("#conversation").show();
}

function sendMessage() {
    stompClient.send("/app/secured/user/send" + url, {}, JSON.stringify(
        {'content': $("#room_msg").val()}
    ));
}

function processMessage(message) {
    showMessage(message.content);
}

function showMessage(message) {
    $("#conversation").append("<div class=\"msg_container\">" + message + "</div>");
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
