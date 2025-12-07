package com.example.myexpirytracker;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    TextView tvAllItems;
    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        tvAllItems = findViewById(R.id.tvAllItems);
        dbHelper = new SQLiteHelper(this);

        List<String> list = dbHelper.getAllItems();

        if (list.isEmpty()) {
            tvAllItems.setText("No items found.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (String item : list) {
                builder.append(item).append("\n\n");
            }
            tvAllItems.setText(builder.toString());
        }
    }
}
