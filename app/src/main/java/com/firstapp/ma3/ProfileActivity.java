package com.firstapp.ma3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class ProfileActivity extends AppCompatActivity {

    public void openZoo(){
        Intent intent = new Intent(this, ZooActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button zooButton = (Button) findViewById(R.id.zooButt);
        zooButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openZoo();
            }
        });
    }
}
