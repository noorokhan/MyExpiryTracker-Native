package com.example.myexpirytracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerCycle;
    EditText etItemName, etDate, etPrice, etNotes, etReminder;
    Button btnSave, btnViewALL, btnDeleteAll;
    TextView tvMessage;

    SQLiteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect UI elements
        spinnerCategory = findViewById(R.id.spinnerType);
        spinnerCycle = findViewById(R.id.spinnerCycle);

        etItemName = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etPrice = findViewById(R.id.etPrice);
        etNotes = findViewById(R.id.etNotes);
        etReminder = findViewById(R.id.etReminder);

        btnSave = findViewById(R.id.btnSave);
        btnViewALL = findViewById(R.id.btnViewAll);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        tvMessage = findViewById(R.id.tvMessage);

        dbHelper = new SQLiteHelper(this);

        // Category Spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Food", "Medicine", "Bill", "Car Insurance", "Other"}
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(typeAdapter);

        // Cycle Spinner
        ArrayAdapter<String> cycleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Daily", "Weekly", "Monthly", "Yearly", "None"}
        );
        cycleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCycle.setAdapter(cycleAdapter);

        // Date Picker
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(
                    MainActivity.this,
                    (DatePicker view, int y, int m, int d) -> etDate.setText(d + "/" + (m + 1) + "/" + y),
                    year, month, day
            );
            datePicker.show();
        });

        // Save Button
        btnSave.setOnClickListener(v -> {
            String category = spinnerCategory.getSelectedItem().toString().trim();
            String itemName = etItemName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String cycle = spinnerCycle.getSelectedItem().toString().trim();
            String price = etPrice.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();
            String reminder = etReminder.getText().toString().trim();

            if (itemName.isEmpty() || date.isEmpty() || reminder.isEmpty()) {
                tvMessage.setText("Please fill all required fields.");
                return;
            }

            long result = dbHelper.insertExpiry(category, itemName, date, cycle, price, notes, reminder);

            if (result > 0) {
                tvMessage.setText("Saved successfully!");
                etItemName.setText("");
                etDate.setText("");
                etPrice.setText("");
                etNotes.setText("");
                etReminder.setText("");
            } else {
                tvMessage.setText("Error saving item.");
            }
        });

        // View All Button
        btnViewALL.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewAllActivity.class));
        });

        // Delete All Button
        btnDeleteAll.setOnClickListener(v -> {
            dbHelper.deleteAll();
            tvMessage.setText("All items deleted.");
        });
    }
}
