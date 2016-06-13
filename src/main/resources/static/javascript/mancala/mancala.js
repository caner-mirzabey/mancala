/**
 * Created by ecanmir on 10.06.2016.
 */
var app = angular.module('mancala', []);


app.controller('MainController', function MainController($scope) {
    $scope.main = {
        root_url: '/game',
        default_game: '/all',
        game: null,
        loggedin: false,
        connected: false,
        connecting: false,
        joining: false,
        joined: false,
        username: '',
        users: [],
        games: [],
        status: '',
    }

    $scope.main.switchLayout = function () {
        var result = !$scope.main.connected || !$scope.main.joined || !$scope.main.connecting;
        atmosphere.util.info("switchLayout?" + result);
        return result;
    }
});

app.controller('LoginController', function LoginController($scope) {
        $scope.login = {
            transport: 'websocket',
            username: '',
            request: null,
            url: $scope.main.root_url + $scope.main.default_game,
            socket: null
        }

        $scope.login.doLogin = function () {
            $scope.connecting = true;
            atmosphere.util.info("doLogin username::" + $scope.login.username);
            $scope.main.username = $scope.login.username;

            $scope.login.request = {
                url: $scope.login.url,
                statusType: 'application/json',
                logLevel: 'debug',
                transport: $scope.login.transport,
                attachHeadersAsQueryString: true,
                headers: {'username': $scope.main.username}
            };

            $scope.login.request.onOpen = function (response) {
                atmosphere.util.info("onOpen::" + response);
                $scope.$apply(function () {
                    $scope.main.loggedin = true;
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                    $scope.main.game = $scope.main.default_game;
                    $scope.login.transport = response.transport;
                    $scope.main.status = 'Atmosphere connected using ' + response.transport;
                });
            };

            $scope.login.request.onClientTimeout = function (response) {
                atmosphere.util.info("onClientTimeout::" + response);
                $scope.login.status = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
                $scope.main.connected = false;
                $scope.main.connecting = true;
                $scope.login.socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.main.username,
                    message: 'is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval
                }));
                setTimeout(function () {
                    $scope.login.socket = atmosphere.subscribe(request);
                }, request.reconnectInterval);
            };

            $scope.login.request.onReopen = function (response) {
                $scope.main.connected = true;
                $scope.main.game = $scope.main.default_game;
                atmosphere.util.info('Atmosphere re-connected using ' + response.transport);
                $scope.main.status = 'Atmosphere re-connected using ' + response.transport;
            };

            //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
            $scope.login.request.onTransportFailure = function (errorMsg, request) {
                atmosphere.util.info(errorMsg);
                request.fallbackTransport = 'long-polling';
                $scope.main.header = 'Atmosphere Mancala Game. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
            };

            $scope.login.request.onMessage = function (response) {
                var responseText = response.responseBody;
                try {
                    atmosphere.util.info(responseText);
                    var message = atmosphere.util.parseJSON(responseText);
                    atmosphere.util.info(message);
                    if (message.users) {
                        $scope.main.users = message.users;
                    }
                    if (message.games) {
                        $scope.main.games = message.games;
                    }
                } catch (e) {
                    console.error("Error parsing JSON: ", responseText);
                    throw e;
                }
            };

            $scope.login.request.onClose = function (response) {
                $scope.main.connecting = true;
                $scope.main.connected = false;
                $scope.main.loggedin = false || $scope.main.joining;
                $scope.main.game = null;
                $scope.main.status = 'Server closed the connection after a timeout';
                $scope.login.socket.push(atmosphere.util.stringifyJSON({
                    author: $scope.login.name,
                    message: 'disconnecting'
                }));
            };

            $scope.login.request.onError = function (response) {
                $scope.main.status = "Sorry, but there's some problem with your socket or the server is down";
                $scope.main.connected = false;
                $scope.connecting = false;
                $scope.main.loggedin = false;
                $scope.main.game = null;
            };

            $scope.login.request.onReconnect = function (request, response) {
                $scope.main.status = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                $scope.main.connecting = true;
                $scope.main.connected = false;
                $scope.main.game = null;
            };

            atmosphere.util.info("subscribe::" + $scope.login.request);
            $scope.login.socket = atmosphere.subscribe($scope.login.request);
        };

        $scope.login.logout = function () {
            atmosphere.util.info("logout")
            $scope.login.socket.unsubscribeUrl($scope.login.url);
        }
    }
);

app.controller('GameController', function GameController($scope) {
        $scope.game = {
            url: $scope.main.root_url,
            transport: 'websocket',
            request: null,
            socket: null,
            name: ''
        }

        $scope.game.join = function () {
            $scope.main.connecting = true;
            $scope.main.joining = true;

            atmosphere.util.info("First unsubscribe from" + $scope.main.root_url + $scope.main.game);
            atmosphere.unsubscribeUrl($scope.main.root_url + $scope.main.game);

            $scope.main.game = $scope.game.name;
            $scope.game.url += '/' + $scope.game.name;

            $scope.game.request = {
                url: $scope.game.url,
                statusType: 'application/json',
                logLevel: 'debug',
                transport: $scope.game.transport,
                attachHeadersAsQueryString: true,
                headers: {'username': $scope.main.username}
            };

            $scope.game.request.onOpen = function (response) {
                $scope.$apply(function () {
                    $scope.game.transport = response.transport;
                    $scope.main.connecting = false;
                    $scope.main.connected = true;
                    $scope.main.joined = true;
                    $scope.main.joining = false;
                    $scope.main.status = 'Atmosphere loggedIn using ' + response.transport;
                });
            };

            $scope.game.request.onClientTimeout = function (response) {
                $scope.$apply(function () {
                    $scope.game.status = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
                    $scope.main.connecting = true;
                    $scope.main.connected = false;
                });

                $scope.game.socket.push(atmosphere.util.stringifyJSON({
                    username: $scope.main.username,
                    message: 'is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval
                }));
                setTimeout(function () {
                    $scope.game.socket = atmosphere.subscribe(request);
                }, request.reconnectInterval);
            };

            $scope.game.request.onReopen = function (response) {
                $scope.$apply(function () {
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                    $scope.main.status = 'Atmosphere re-loggedIn using ' + response.transport;
                });
            };

            //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
            $scope.game.request.onTransportFailure = function (errorMsg, request) {
                atmosphere.util.info(errorMsg);
                request.fallbackTransport = 'long-polling';
                $scope.main.header = 'Atmosphere Mancala Game. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
            };

            $scope.game.request.onMessage = function (response) {
                var responseText = response.responseBody;
                try {
                    atmosphere.util.info(responseText);
                    var message = atmosphere.util.parseJSON(responseText);
                    atmosphere.util.info(message);
                    if (message.users) {
                        $scope.main.users = message.users;
                    }
                    if (message.games) {
                        $scope.main.games = message.games;
                    }
                } catch (e) {
                    console.error("Error parsing JSON: ", responseText);
                    throw e;
                }
            };

            $scope.game.request.onClose = function (response) {
                $scope.main.status = 'Server closed the connection after a timeout';

                $scope.game.socket.push(atmosphere.util.stringifyJSON({
                    author: $scope.main.username,
                    message: 'disconnecting'
                }));
                $scope.$apply(function () {
                    $scope.main.connected = false;
                    $scope.main.connecting = false;
                    $scope.main.joined = false;
                    $scope.main.joining = false;
                    $scope.main.game = '';
                });
            };

            $scope.game.request.onError = function (response) {
                $scope.$apply(function () {
                    $scope.main.status = "Sorry, but there's some problem with your socket or the server is down";
                    $scope.main.connected = false;
                    $scope.main.connecting = false;
                    $scope.main.joined = false;
                    $scope.main.joining = false;
                    $scope.main.game = '';
                });
            };

            $scope.game.request.onReconnect = function (request, response) {
                $scope.$apply(function () {
                    $scope.main.status = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                    $scope.main.connected = true;
                    $scope.main.connecting = false;
                });
            };

            atmosphere.util.info("subscribe::" + $scope.game.request)
            $scope.game.socket = atmosphere.subscribe($scope.game.request);
        };

        $scope.game.leave = function () {
            $scope.game.socket.unsubscribe($scope.game.url);
            $scope.main.joined = false;
            $scope.main.connecting = false;
            $scope.login.doLogin();
        }
    }
);

