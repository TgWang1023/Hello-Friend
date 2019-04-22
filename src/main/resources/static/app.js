var stompClient = null;
var sessionId = "";
var url = null

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
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
                showMessage(JSON.parse(greeting.body).content);
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

function sendName() {
    stompClient.send("/app/secured/user/hello" + url, {}, JSON.stringify(
        {'name': $("#hello_name").val()}
    ));
}

// TODO
function createRoom() {
    stompClient.send("/app/secured/user/connect" + url, {}, JSON.stringify(
        {'userName': $("#user_name").val(),
        'roomName': $("#room_name").val(),
        'userLanguage': $("#user_lang").val(),
        'request': 1}
    ));
}

// TODO
function joinRoom() {
    stompClient.send("/app/secured/user/connect" + url, {}, JSON.stringify(
        {'userName': $("#user_name").val(),
        'roomName': $("#room_name").val(),
        'userLanguage': $("#user_lang").val(),
        'request': 2}
    ));
}

/*
function translate() {
    stompClient.send("/app/translate_message", {}, JSON.stringify(
        {'messageCH': $("#msgCH").val(),
        'messageEN': $("#msgEN").val()}
    ));
}*/

function showMessage(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#create_room" ).click(function() { createRoom(); })
    $( "#join_room" ).click(function() { joinRoom(); })
});
