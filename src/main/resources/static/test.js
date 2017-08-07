var stompClient = null;

$(document).ready(function () {
    connect();
});

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        setConnected(true);
        stompClient.subscribe('/chat', function (message) {
            var body = JSON.parse(message.body);
            showGreeting(body.sender.login + ' - ' + body.text);
        });
        stompClient.subscribe('/user/queue/reply', function (message) {
            var head = JSON.parse(message.body);
            showGreeting(head.sender.login + ' - ' + head.text);
        });
        stompClient.subscribe('/user/search/reply', function (message) {
            drawUsers(JSON.parse(message.body));
        });
    });
}

function drawUsers(list) {
    $('#users tr').remove();
    list.forEach(function (item) {
        $("#users").append('<tr><td>' + item.id + '</td><td>' + item.login + '</td></tr>');
    });
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
}


function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

function sendForAll() {
    stompClient.send("/app/hello", {}, JSON.stringify({'text': $("#name").val()}));
    $("#name").val("");
}

function sendToUser() {
    var selected = $(".selected td");
    var recipient = {
        'id': selected[0].innerText,
        'login': selected[1].innerText
    };
    var message = {
        'text': $("#name").val(),
        'recipient': recipient
    };
    stompClient.send("/app/hellouser", {}, JSON.stringify(message));
    $("#name").val("");
}

function send() {
    if ($(".selected").length) {
        sendToUser();
    } else {
        sendForAll();
    }
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

//Key mapped
$(document).ready(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        send();  //fixme
    });
});

function findUsers() {
    if ($('#key').val().length > 0) {
        stompClient.send("/app/findusers", {}, $("#key").val());
    }
}


//Live search of users
$(document).ready(function () {
    $("#find").click(findUsers);
    $("#key").keyup(findUsers);
});

//On recipient select
$(document).ready(function () {
    $('#users').delegate("tr", "click", function () {
        if ($(this).hasClass("selected")) {
            $('#users tr').removeAttr('class style');  // repeating of this string is important
        } else {
            $('#users tr').removeAttr('class style');
            $(this).css({'background-color': 'cyan', 'opacity': '0.3'});
            $(this).addClass("selected");
        }
    });
});


