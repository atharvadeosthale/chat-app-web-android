package com.example.atharvachat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText usernameBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameBox = (EditText) findViewById(R.id.username);
    }

    public void goToChat(View view) {
        Intent i = new Intent(getBaseContext(), ChatActivity.class);
        String username = usernameBox.getText().toString();
        i.putExtra("UserName", username);
        startActivity(i);
    }
}
