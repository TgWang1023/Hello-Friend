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
                showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

// experiment
function initializeRoom() {
    stompClient.send("/app/secured/user/send" + url, {}, JSON.stringify({
        'roomNumber': $("#roomNumber").val(), 'roomMessage': sessionId
    }));
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendRoom() {
    stompClient.send("/app/room_chat", {}, JSON.stringify({'roomNumber': $("#roomNumber").val(),
                                                        'roomMessage': $("#roomMessage").val()}));
}

// experiment
function sendUserChannel() {
    stompClient.send("/app/secured/user/send" + url, {}, JSON.stringify({'roomNumber': $("#roomNumber").val(),
                                                        'roomMessage': $("#roomMessage").val()}));
}

function translate() {
    stompClient.send("/app/translate_message", {}, JSON.stringify({'messageCH': $("#msgCH").val(),
                                                                    'messageEN': $("#msgEN").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#room_send" ).click(function() { initializeRoom(); }) // sendRoom()
    $( "#trans_send" ).click(function() { translate(); })
});
