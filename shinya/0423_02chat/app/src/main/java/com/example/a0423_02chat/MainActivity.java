package com.example.a0423_02chat;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;

    private String currentUser = "user1"; // 自分のID（user1 or user2）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // 送信ボタンが押されたときの処理
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString().trim();
                if (!message.isEmpty()) {
                    Message newMessage = new Message(currentUser, message);
                    messageList.add(newMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    messageInput.setText("");

                    // メッセージ送信後、5秒後に受信メッセージをシミュレート
                    simulateReceivedMessage();
                }
            }
        });
    }

    // 受信メッセージをシミュレートするメソッド
    private void simulateReceivedMessage() {
        // 5秒後にメッセージを受信したと仮定して処理
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String receivedMessage = "相手からのメッセージ";
                // メッセージ受信者は「user2」
                Message newMessage = new Message("user2", receivedMessage);
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
            }
        }, 5000); // 5秒後にメッセージを受信
    }
}