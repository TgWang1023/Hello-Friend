var stompClient = null;

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
    var socket = new SockJS('/hello-friend');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/room/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
//        stompClient.subscribe('/user/queue/user_reply', function (greeting) {
//            showGreeting(JSON.parse(greeting.body).content);
//        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendRoom() {
    stompClient.send("/app/room_chat", {}, JSON.stringify({'roomNumber': $("#roomNumber").val(),
                                                        'roomMessage': $("#roomMessage").val()}));
}

function sendUserChannel() {
    stompClient.send("/app/user_channel", {}, JSON.stringify({'roomNumber': $("#roomNumber").val(),
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
    $( "#room_send" ).click(function() { sendRoom(); }) // sendRoom()
    $( "#trans_send" ).click(function() { translate(); })
});