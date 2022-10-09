package com.example.week3project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        PostAdapter adapter = new PostAdapter(this, posts);
        listView.setAdapter(adapter);

    }


    postBtn = (Button) findViewById(R.id.postBtn);
    postBtn.setOnClickListener(new View.OnClickListener(){

        @Override
        public void onClick (View view){
            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivityForRsult(intent, POST_REQEST);
        }
    });



}