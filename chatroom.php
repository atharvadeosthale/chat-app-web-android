<?php
$username = $_GET["username"];
?>
<html>
<head>
    <title>Chatroom</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/socket.io/2.3.0/socket.io.js"></script>
    <script type="text/javascript">
        var username = "<?php echo $username; ?>";
        var io = io("ws://3.6.233.23:8237");
        io.emit('ident', username, function(){
            console.log("Joined the chatroom");
        });
        io.on('new-msg', function(data){
            document.getElementById("chatbox").innerHTML += "<b>" + data.by + ":</b> " + data.message + "<br>";
        });
        io.on('user-join', function(data){
            document.getElementById("chatbox").innerHTML += "<b>" + data.username + "</b> joined the chat!<br>";
        });
        function sendmsg(){
            var message = document.getElementById("msg").value;
            io.emit('msg', message, function(){
                console.log("Message sent to server!");
            });
            document.getElementById("msg").value = "";
        }
    </script>
</head>
<body>
    <p id="chatbox"></p>
    Enter your message: <input type="text" id="msg">&nbsp;
    <button onclick="sendmsg()">Send</button>
</body>
</html>
