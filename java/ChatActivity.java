package com.example.atharvachat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    TextView tv;
    Button btn;
    EditText chat;
    Socket mSocket;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tv=(TextView) findViewById(R.id.chatbox);
        btn = (Button) findViewById(R.id.send);
        chat = (EditText) findViewById(R.id.msg);

        tv.setMovementMethod(new ScrollingMovementMethod());

        Bundle extras = getIntent().getExtras();
        username = (String) extras.getString("UserName");

        SocketAdapter socketAdapter = new SocketAdapter();
        mSocket = socketAdapter.getSocket();
        mSocket.on("new-msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                final String by = (String) json.optString("by");
                final String message = (String) json.optString("message");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append(by + ": " + message + "\n");
                    }
                });
            }
        });
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append("Connection to the server lost!\n");
                    }
                });
            }
        });
        mSocket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append("Reconnected to the server!\n");
                    }
                });
            }
        });
        mSocket.on(Socket.EVENT_CONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append("Connecting to the server....\n");
                    }
                });
            }
        });
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mSocket.emit("ident", username);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append("Connected to chat as " + username + ". Enjoy chatting!\n");
                    }
                });
            }
        });
        mSocket.on("user-join", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                final String name = (String) json.optString("username");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append(name + " joined the chat!\n");
                    }
                });
            }
        });
        mSocket.on("user-left", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject json = (JSONObject) args[0];
                final String name = (String) json.optString("username");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.append(name + " left the chat\n");
                    }
                });
            }
        });
        mSocket.connect();
    }
    public void sendMsg(View view) {
        mSocket.emit("msg", chat.getText());
        chat.setText("");
    }
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
