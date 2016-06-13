$(function () {
    "use strict";

    var header = $('#header');
    var rooms = $('#rooms');
    var content = $('#content');
    var users = $('#user');
    var input = $('#input');
    var status = $('#status');
    var myName = false;
    var author = null;
    var logged = false;
    var socket = atmosphere;
    var subSocket;
    var transport = 'websocket';
    var fallbackTransport = 'long-polling'
    var connected = false;
    var uuid = 0;

    header.html($('<h3>', {text: 'Atmosphere Chat. Default transport is ' + transport + ', fallback is ' + fallbackTransport}));
    status.text('Choose chatroom:');
    input.removeAttr('disabled').focus();

    input.keydown(function (e) {
        if (e.keyCode === 13) {
            console.log('Enter');
            console.log($(this).val());

            var msg = $(this).val();

            $(this).val('');
            if (!connected) {
                console.log('Connecting...');
                connected = true;
                connect(msg);
                return;
            }

            // First message received is always the author's name
            if (author == null) {
                author = msg;
            }

            input.removeAttr('disabled').focus();
            // Private message
            if (msg.indexOf(":") !== -1) {
                var a = msg.split(":")[0];
                subSocket.push(atmosphere.util.stringifyJSON({user: a, message: msg}));
            } else {
                subSocket.push(atmosphere.util.stringifyJSON({author: author, message: msg, uuid: uuid}));
            }

            if (myName === false) {
                myName = msg;
            }
        }
    });

    function connect(chatroom) {
        // We are now ready to cut the request
        var request = {
            url: '/chat-room/' + chatroom,
            contentType: "application/json",
            logLevel: 'debug',
            transport: transport,
            trackMessageLength: true,
            reconnectInterval: 5000,
            timeout: 10000,
            fallbackTransport: fallbackTransport,
            attachHeadersAsQueryString: true,
            headers: {'username': myName}
        };

        request.onOpen = function (response) {
            content.html($('<p>', {text: 'Atmosphere loggedIn using ' + response.transport}));
            status.text('Choose name:');
            input.removeAttr('disabled').focus();
            transport = response.transport;
            uuid = response.request.uuid;
        };

        request.onReopen = function (response) {
            content.html($('<p>', {text: 'Atmosphere re-loggedIn using ' + response.transport}));
            input.removeAttr('disabled').focus();
        };

        request.onMessage = function (response) {
            console.log(response);
            var message = response.responseBody;
            console.log(message);
            try {
                var json = atmosphere.util.parseJSON(message);
                console.log(json);
            } catch (e) {
                console.log('This doesn\'t look like a valid JSON: ', message);
                return;
            }

            input.removeAttr('disabled').focus();
            if (json.rooms) {
                rooms.html($('<h2>', {text: 'Current room: ' + chatroom}));

                var r = 'Available rooms: ';
                for (var i = 0; i < json.rooms.length; i++) {
                    r += json.rooms[i] + "  ";
                }
                rooms.append($('<h3>', {text: r}))
            }

            if (json.users) {
                var r = 'Connected user: ';
                for (var i = 0; i < json.users.length; i++) {
                    r += json.users[i] + "  ";
                }
                users.html($('<h3>', {text: r}))
            }

            if (json.author) {
                if (!logged && myName) {
                    logged = true;
                    status.text(myName + ': ').css('color', 'blue');
                } else {
                    var me = json.author == author;
                    var date = typeof(json.time) == 'string' ? parseInt(json.time) : json.time;
                    addMessage(json.author, json.message, me ? 'blue' : 'black', new Date(date));
                }
            }
        };

        request.onClose = function (response) {
            subSocket.push(atmosphere.util.stringifyJSON({author: author, message: 'disconnecting'}));
        };

        request.onError = function (response) {
            content.html($('<p>', {
                text: 'Sorry, but there\'s some problem with your '
                + 'socket or the server is down'
            }));
            logged = false;
        };

        request.onReconnect = function (request, response) {
            content.html($('<p>', {text: 'Connection lost, trying to reconnect. Trying to reconnect ' + request.reconnectInterval}));
            input.attr('disabled', 'disabled');
        };

        console.log('subscriber');
        console.log(eval(request));
        subSocket = socket.subscribe(request);
    }

    function addMessage(author, message, color, datetime) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' + +(datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
            + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
            + ': ' + message + '</p>');
    }
});
