package com.example.growtime;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.ComponentActivity;

import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class RecommendSceneActivity extends ComponentActivity {

    EditText zipcode_input;
    TextView zip_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommend_scene);

        zipcode_input = findViewById(R.id.zipcode_input);
        zip_res = findViewById(R.id.zip_res);
    }

    public void updateText(View view){
        String zip = zipcode_input.getText().toString();
        String res = "Zip: " + zip;
        zip_res.setText(res);
    }
}