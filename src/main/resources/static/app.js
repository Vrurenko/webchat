var stompClient = null;

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        setConnected(true);
        stompClient.subscribe('/user/typing/reply', function (message) {
            var body = JSON.parse(message.body);
            if (getCurrentDiscussion() == body.discussionID) {
                showWhoTyping(body.login);
            }
        });
        stompClient.subscribe('/user/loadMessages/reply', function (message) {
            var messageList = JSON.parse(message.body);
            var messages = '<div class="messagePage">';
            messageList.forEach(function (message) {
                messages += appendMessage(message);
            });
            messages += '</div>';
            if (messageList.length) {
                $(".message-container").prepend(messages);
            }
            if (getNextPageNumber() == 2) {
                var div = $(".message-container");
                div.animate({scrollTop: div.prop("scrollHeight") - div.height()}, 0);
            }
        });
        stompClient.subscribe('/user/search/reply', function (message) {
            var users = JSON.parse(message.body);
            $("#user-container .user:visible").remove();
            users.forEach(function (item) {
                appendUser(item);
            });
        });
        stompClient.subscribe('/user/newDiscussion/reply', function (message) {
            var body = JSON.parse(message.body);
            prependDiscussion(body.id, body.users);
        });
        stompClient.subscribe('/user/newMessage/reply', function (message) {
            var newMessage = JSON.parse(message.body);
            $(".message-container").append(appendMessage(newMessage));
            var div = $(".message-container");
            div.animate({scrollTop: div.prop("scrollHeight") - div.height()}, 0);
        });
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

function toHumanEyes(date) {
    return date.toLocaleTimeString() + ' ' + date.toLocaleDateString();
}

function setModalWindowHandler() {
    $('#newDiscussion').click(function (event) {
        event.preventDefault();
        $('#overlay').fadeIn(200, // снaчaлa плaвнo пoкaзывaем темную пoдлoжку
            function () { // пoсле выпoлнения предъидущей aнимaции
                $('#modal_form')
                    .css('display', 'block') // убирaем у мoдaльнoгo oкнa display: none;
                    .animate({opacity: 1, top: '50%'}, 200); // плaвнo прибaвляем прoзрaчнoсть oднoвременнo сo съезжaнием вниз
            });
    });
    /* Зaкрытие мoдaльнoгo oкнa, тут делaем тo же сaмoе нo в oбрaтнoм пoрядке */
    $('#modal_close, #overlay').click(function () { 
        $('#modal_form')
            .animate({opacity: 0, top: '45%'}, 200,  // плaвнo меняем прoзрaчнoсть нa 0 и oднoвременнo двигaем oкнo вверх
                function () { // пoсле aнимaции
                    $(this).css('display', 'none'); // делaем ему display: none;
                    $('#overlay').fadeOut(200); // скрывaем пoдлoжку
                }
            );
    });
}

$(document).ready(function () {
    connect();
    setModalWindowHandler();
});

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
});
//Live search of users
$(document).ready(function () {
    $("#user-search").click(findUsers);
    $("#user-search-key").keyup(findUsers);
});

function appendUser(item) {
    var isExistAllReady = $("#user-" + item.id).length > 0;
    if (!isExistAllReady) {
        var newUser = "<div id='user-" + item.id + "' class='user'>" +
            "<div class='col-md-12'>" +
            "<div class='row item'>" +
            "<div class='col-md-2'>" +
            "<img src='http://localhost:8080/image/user/" + item.id + "' class='round'/>" +
            "</div><div class='col-md-8 message-text'>" + item.login +
            "</div><div class='col-md-2'>" +
            "<button class='btn btn-default' type='button'>" +
            "<span class='glyphicon glyphicon-plus'></span>" +
            "</button></div></div></div></div>";
        $("#user-container").append(newUser);
    }
}

function appendToken(id, login) {
    var newToken = "<div class='token'>" +
        "<div class='token-login'>" + login + "</div>" +
        "<div class='token-x'>" +
        "<span id='token-" + id + "' class='glyphicon glyphicon-remove pull-right x-remove'></span>" +
        "</div></div>";
    $(".token-container").append(newToken);
}

function prependDiscussion(id, users) {
    var discussionName = '';
    users.forEach(function (item) {
        discussionName += item.login + ', ';
    });
    discussionName = discussionName.slice(0, -2);
    var newDiscussion = "<div class='discussion' id='discussion-" + id + "'>" +
        "<div class='col-md-12'>" +
        "<div class='row item'>" +
        "<div class='col-md-2'>" +
        "<img class='round' src='http://localhost:8080/image/user/" + id + "'/>" +
        "</div>" +
        "<div class='col-md-10 discussion-name'>" +
        "<div class='col-md-10 d-name'>" + discussionName + "</div>" +
        "<div class='col-md-2'>" +
        "<span class='glyphicon glyphicon-remove pull-right x-remove'></span>" +
        "</div></div></div></div></div>";
    $(".discussion-container").prepend(newDiscussion);
}

function appendMessage(message) {
    var newMessage = "<div class='message'>" +
        "<div class='col-md-12'>" +
        "<div class='row item'>" +
        "<div class='col-md-2'>" +
        "<img src='http://localhost:8080/image/user/" + message.sender.id + "' class='round'/>" +
        "</div><div class='col-md-10 message-text'>" + message.text + ' - ' + toHumanEyes(new Date(message.date)) +
        "</div></div></div></div>";
    return newMessage;
}

$(document).ready(function () {
    $("#user-container").delegate("button", "click", function () {
        var src = $(this).parents("div.item").children().find("img").attr("src");
        var id = src.substr(src.lastIndexOf('/') + 1, src.length);
        var login = $(this).parents("div.item").children(".message-text")[0].innerText;
        appendToken(id, login);
        $(this).parents(".user").hide();
    });

    $("div.token-container").delegate("span", "click", function () {
        var id = $(this).attr("id");
        id = id.substr(id.lastIndexOf('-') + 1, id.length);
        $(this).parent().parent().remove();
        $("#user-" + id).show();
    });

    $("#accept").click(function () {
        var ids = $(".token span").map(function () {
            var id = $(this).attr("id");
            return id.substr(id.indexOf('-') + 1, id.length);
        }).get();
        createDiscussion(ids);
        $(".token-container").empty();
        $("#modal_close").click();
    });

    $(".discussion-container").delegate(".discussion", "click", function () {
        var id = $(this).attr("id");
        id = id.substr(id.lastIndexOf("-") + 1, id.length);
        var name = $(this).find(".d-name")[0].innerText;
        $(".discussion-header").text(name).attr('id', 'discussion-' + id);
        displayMessages(id);
    });

    $("#sendMessage").click(function () {
        sendMessage();
        $("#message").val('');
    });

    $("#findDiscussion").keyup(findDiscussion);

    $("#clear").click(function () {
        $("#findDiscussion").val('');
        findDiscussion();
    });

    $(".message-container").on("scroll", function () {
        if ($(this).scrollTop() == 0) {
            loadMessages(getCurrentDiscussion());
        }
    });

    $("#message").keyup(function () {
        console.log($("#message").val().length);
        if ($("#message").val().length % 10 == 0) {
            stompClient.send("/app/typing", {}, JSON.stringify({'id': getCurrentDiscussion()}));
        }
    })

});

function createDiscussion(ids) {
    if (ids.length > 0) {
        var users = [];
        ids.forEach(function (id) {
            users.push({'id': id, 'login': '', 'password': ''})
        });
        console.log(ids);
        var discussion = {'id': '', 'messages': '', 'users': users};
        console.log(discussion);
        stompClient.send("/app/addDiscussion", {}, JSON.stringify(discussion));
    }
}

function displayMessages(id) {
    $(".message-container").empty();
    loadMessages(id);
}

function loadMessages(id) {
    var messagePage = {
        'discussionID': id,
        'page': getNextPageNumber()
    };
    stompClient.send("/app/loadMessages", {}, JSON.stringify(messagePage));
}

function findUsers() {
    var value = $('#user-search-key').val();
    if (value.length > 0) {
        stompClient.send("/app/findusers", {}, value);
    }
}

function sendMessage() {
    var id = $(".discussion-header").attr("id");
    var discussion = {'id': id.substr(id.lastIndexOf('-') + 1, id.length)};
    var message = {
        'id': '',
        'text': $("#message").val(),
        'date': null,
        'sender': null,
        'recipient': discussion
    };
    stompClient.send("/app/newMessage", {}, JSON.stringify(message));
}

function findDiscussion() {
    var key = $("#findDiscussion").val();
    $('.discussion').show();
    if (key.length > 0) {
        $('.discussion .d-name:not(:contains("' + key + '"))').parents('.discussion').hide();
    }

}

function getNextPageNumber() {
    return $(".messagePage").length + 1;
}

function getCurrentDiscussion() {
    var id = $(".discussion-header").attr("id");
    return id.substr(id.lastIndexOf("-") + 1, id.length);
}

function showWhoTyping(name) {
    $("#typing").text(name + " is typing...");
    $("div#typing").fadeIn(100).delay(1000).fadeOut(1000);
}


