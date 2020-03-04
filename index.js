const http = require('http').createServer();
const io = require('socket.io')(http);

io.on('error', function(){
    console.log("Someone tried to tamper the websocket");
});

io.on("connection", function(socket){
    var username;
    console.log("User connected to server - " + socket.id)
    socket.on('kick', function(data) {
        io.sockets.connected[data].disconnect();
    })
    socket.on('ident', function(data){
        username = data;
        socket.broadcast.emit('user-join', {username: username});
        console.log(username + " joined the chat!");
    });
    socket.on('msg', function(data){
        io.emit('new-msg', {by: username, message: data});
        console.log(username + ": " + data);
    });
    socket.on('disconnect', function(){
        io.emit('user-left', {username: username});
        console.log(username + " disconnected from chat!");
    });
    socket.on('error', function(){
        console.log("Someone tried to tamper the websocket");
    });
});

http.listen(8237, function(){
    console.log("Server is listening at port 8237");
});