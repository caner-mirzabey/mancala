/**
 * Created by ecanmir on 10.06.2016.
 */
var app = angular.module('mancala', []);

app.controller('MainController', function MainController($scope) {
    $scope.main = {
        user: null,
        status: '',
        loggedIn: false,
        connected: false,
        connecting: false,
        joining: false,
        joined: false,
        users: [],
        games: []
    };

    $scope.main.switchLayout = function () {
        var result = !$scope.main.connected || !$scope.main.joined || !$scope.main.connecting;
        console.log('switchLayout?' + result);
        return result;
    };
});

app.controller('LoginController', function LoginController($scope) {
        $scope.login = {
            username: ''
        };

        var socket = null;

        $scope.login.doLogin = function () {
            console.log('doLogin()');

            $scope.connecting = true;
            $scope.main.username = $scope.login.username;
            console.log('username::' + $scope.login.username);

            var loginRequest = new Request(LOGIN_GAME, $scope.login.username);

            loginRequest.onOpen = function (response) {
                console.log('onOpen');
                console.log(response);


                $scope.$apply(function () {
                    $scope.main.loggedin = true;
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                    $scope.main.user = new User(response.uuid, $scope.login.username);
                    $scope.main.status = 'WebSocket connected using ' + response.transport;
                });
            };

            loginRequest.onClientTimeout = function (response) {
                console.log('onClientTimeout');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.connected = false;
                    $scope.main.connecting = true;
                    $scope.login.status = 'Client closed the connection after a timeout. Reconnecting in ' + loginRequest.reconnectInterval;
                });

                socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.main.username,
                    message: 'onClientTimeout:: ' + $scope.main.username + ' is inactive and closed the connection. Will reconnect in ' + loginRequest.reconnectInterval
                }));

                setTimeout(function () {
                    console.log('setTimeout, subscribing again...');
                    console.log(loginRequest);
                    socket = atmosphere.subscribe(loginRequest);
                }, loginRequest.reconnectInterval);
            };

            loginRequest.onReopen = function (response) {
                console.log('onReopen');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.loggedin = true;
                    $scope.main.connected = true;
                    $scope.main.uuid = response.uuid;
                    $scope.main.status = 'WebSocket re-connected using ' + response.transport;
                });
            };

            //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
            loginRequest.onTransportFailure = function (errorMsg, request) {
                console.log('onTransportFailure');
                console.log(errorMsg);
                console.log(request);
                loginRequest.fallbackTransport = 'long-polling';
                $scope.main.header = 'WebSocket Mancala Game. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
            };

            loginRequest.onMessage = function (response) {
                console.log('onMessage');
                console.log(response);

                var responseText = response.responseBody;
                try {
                    console.log(responseText);
                    var message = atmosphere.util.parseJSON(responseText);
                    console.log(message);
                    $scope.$apply(function () {
                        if (message.users) {
                            $scope.main.users = message.users;
                        }
                        if (message.games) {
                            $scope.main.games = message.games;
                        }
                    });
                } catch (e) {
                    console.error('Error parsing JSON: ', responseText);
                    throw e;
                }
            };

            loginRequest.onClose = function (response) {
                console.log('onClose');
                console.log(response);

                // $scope.$apply(function () {
                $scope.main.connecting = true;
                $scope.main.connected = false;
                $scope.main.loggedin |= $scope.main.joining;
                $scope.main.uuid = '';
                $scope.main.status = 'Server closed the connection after a timeout';
                // });

                socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.login.name,
                    message: 'onClose::Disconnecting'
                }));
            };

            loginRequest.onError = function (response) {
                console.log('onError');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.status = "Sorry, but there's some problem with your socket or the server is down";
                    $scope.main.connected = false;
                    $scope.connecting = false;
                    $scope.main.loggedin = false;
                });
            };

            loginRequest.onReconnect = function (request, response) {
                console.log('onError');
                console.log(request);
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.status = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                    $scope.main.connecting = true;
                    $scope.main.connected = false;
                });
            };

            console.log('subscribe');
            socket = atmosphere.subscribe(loginRequest);
        };

        $scope.login.logout = function () {
            console.log('logout');
            atmosphere.unsubscribe();
        }
    }
);


app.controller('GameController', function GameController($scope) {
        $scope.game = {
            name: '',
            game: null
        };

        var socket = null;
        var gameRequest = null;

        $scope.game.join = function () {
            $scope.main.connecting = true;
            $scope.main.joining = true;

            console.log('First unsubscribe from::' + LOGIN_GAME);
            $scope.login.logout();


            gameRequest = new Request($scope.game.name, $scope.main.username);

            gameRequest.onOpen = function (response) {
                console.log('onOpen');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.connecting = false;
                    $scope.main.connected = true;
                    $scope.main.joined = true;
                    $scope.main.joining = false;
                    $scope.main.uuid = response.uuid;
                    $scope.main.status = 'Joined to the game ' + $scope.game.name + ' using ' + response.transport;
                });
            };

            gameRequest.onClientTimeout = function (response) {
                console.log('onClientTimeout');
                console.log(response);

                $scope.$apply(function () {
                    $scope.game.status = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
                    $scope.main.connecting = true;
                    $scope.main.connected = false;
                });

                socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.main.username,
                    message: 'onClientTimeout::is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval
                }));
                setTimeout(function () {
                    socket = atmosphere.subscribe(gameRequest);
                }, gameRequest.reconnectInterval);
            };

            gameRequest.onReopen = function (response) {
                console.log('onReopen');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                    $scope.main.joined = true;
                    $scope.main.joining = false;
                    $scope.main.uuid = response.uuid;
                    $scope.main.status = 'Re-loggedIn using ' + response.transport;
                });
            };

            //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
            gameRequest.onTransportFailure = function (errorMsg, request) {
                console.log('onTransportFailure');
                console.log(errorMsg);
                console.log(request);

                gameRequest.fallbackTransport = 'long-polling';
                $scope.main.header = 'WebSocket Mancala Game. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
            };

            gameRequest.onMessage = function (response) {
                console.log('onMessage');
                console.log(response);

                var responseText = response.responseBody;
                try {
                    console.log(responseText);
                    var message = atmosphere.util.parseJSON(responseText);
                    console.log(message);
                    $scope.$apply(function () {
                        if (message.users) {
                            $scope.main.users = message.users;
                        }
                        if (message.games) {
                            $scope.main.games = message.games;
                        }
                    });
                } catch (e) {
                    console.error('Error parsing JSON: ', responseText);
                    throw e;
                }
            };

            gameRequest.onClose = function (response) {
                console.log('onClose');
                console.log(response);

                socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.main.username,
                    message: 'onClose::disconnecting'
                }));

                // $scope.$apply(function () {
                $scope.main.status = 'Server closed the connection after a timeout';
                $scope.main.connected = false;
                $scope.main.connecting = false;
                $scope.main.joined = false;
                $scope.main.joining = false;
                $scope.main.uuid = '';
                // });
            };

            gameRequest.onError = function (response) {
                console.log('onError');
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.status = "Sorry, but there's some problem with your socket or the server is down";
                    $scope.main.connected = false;
                    $scope.main.connecting = false;
                    $scope.main.joined = false;
                    $scope.main.joining = false;
                    $scope.main.uuid = '';
                });
            };

            gameRequest.onReconnect = function (request, response) {
                console.log('onError');
                console.log(request);
                console.log(response);

                $scope.$apply(function () {
                    $scope.main.status = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                });
            };

            console.log('subscribe::');
            console.log(gameRequest);
            socket = atmosphere.subscribe(gameRequest);
        };

        $scope.game.leave = function () {
            socket.unsubscribe($scope.game.url);
            $scope.login.doLogin();
        };

        $scope.game.sendCommand = function () {
            var username = $scope.main.username;
            var message = $scope.game.message;
            var gameCommand = new GameCommand(1, username, message);
            console.log('Send command::' + message + ', name::' + name);
            console.log(gameCommand);
            var gameCommandString = atmosphere.util.stringifyJSON(gameCommand);
            console.log('Send command::' + gameCommandString);
            socket.push(gameCommandString);
        };

        $scope.game.sendMessage = function () {
            var username = $scope.main.username;
            var message = $scope.game.message;
            var gameMessage = new GameMessage(username, message);
            console.log('Send message::' + message + ', name::' + name);
            console.log(gameMessage);
            var gameMessageString = atmosphere.util.stringifyJSON(gameMessage);
            console.log('Send message::' + gameMessageString);
            socket.push(gameMessageString);
        };

        var GameMessage = function (name, message) {
            this.name = name;
            this.message = message;
        };

        var GameCommand = function (move, username, message) {
            this.move = move;
            this.message = message;
            this.username = username;
        };
    }
);

var Request = function (game, username) {
    this.url = ROOT_URI + game;
    this.contentType = 'application/json';
    this.logLevel = 'debug';
    this.transport = 'websocket';
    this.fallbackTransport = 'long-polling';
    this.connectTimeout = 20000;
    this.reconnectInterval = 2000;
    this.attachHeadersAsQueryString = true;
    this.headers = {'username': username};
};

var User = function (uuid, username) {
    this.uuid = uuid;
    this.username = username;

    function setUUID(uuid) {
        this.uuid = uuid;
    }
};

const ROOT_URI = '/game/';
const LOGIN_GAME = 'all';
