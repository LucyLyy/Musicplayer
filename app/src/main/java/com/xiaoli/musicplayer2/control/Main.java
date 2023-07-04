package com.xiaoli.musicplayer2.control;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.xiaoli.musicplayer2.R;


import com.xiaoli.musicplayer2.utils.GetMenuList;
import com.xiaoli.musicplayer2.utils.CRUD;

import java.util.ArrayList;


public class Main extends AppCompatActivity {
    private EditText mEditText;
    private static final String TAG = "Main";
  //  private TextView bottom_music_name;
    private String mKeyWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }
    private void initView() {
    //    bottom_music_name=findViewById(R.id.tv_bottom_song_name);
        mEditText = findViewById(R.id.keywords);
    }
    public void like(View view){
        if(new CRUD().likeSelete(getApplicationContext(),10)){
            Intent intent = new Intent(Main.this,Result.class);
            intent.putExtra("Tag","我喜欢的音乐");
            intent.putExtra("keyword","");
            startActivity(intent);
        }else {
            Toast.makeText(this, "喜欢的音乐为空", Toast.LENGTH_SHORT).show();
        }
    }
    public void list(View view){
        if(new CRUD().listSelete(getApplicationContext(),10)){
            Intent intent = new Intent(Main.this,Result.class);
            intent.putExtra("Tag","音乐列表");
            intent.putExtra("keyword","");
            startActivity(intent);
        }else {
            Toast.makeText(this, "音乐列表为空", Toast.LENGTH_SHORT).show();
        }
    }
    public void dlList(View view){startActivity(new Intent(Main.this,Download.class));}
    public void search(View view) {
        mKeyWord = mEditText.getText().toString();
        if(mKeyWord ==null|| mKeyWord.equals("")){
            Toast.makeText(this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
        }else {
            try {
                new GetMenuList(30).getMusicList(mKeyWord);
                Intent intent = new Intent(Main.this, Result.class);
                intent.putExtra("Tag", "搜索：");
                intent.putExtra("keyword", mKeyWord);
                startActivity(intent);
            } catch (Exception e){
                finish();
                Toast.makeText(this,"请输入正确的音乐名称！",Toast.LENGTH_LONG);
            }
        }
    }
}
