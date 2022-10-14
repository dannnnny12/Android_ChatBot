package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatModel>chatModelArrayList;
    private ChatRVAdapter chatRVAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);
        chatModelArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);
        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter your Message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");
            }
        });


    }
    private void getResponse(String message){
        chatModelArrayList.add(new ChatModel(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url="http://api.brainshop.ai/get?bid=169811&key=ab55P8z7fcry2f5Y&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retorfit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        RetroFitAPI retrofitAPI = retorfit.create(RetroFitAPI.class);
        Call<MessageModel> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        MessageModel model = response.body();
                        chatModelArrayList.add(new ChatModel(model.getCnt(),BOT_KEY));
                        chatRVAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                chatModelArrayList.add(new ChatModel("Please revert your question",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }
}