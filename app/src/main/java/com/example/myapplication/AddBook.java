package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AddBook extends AppCompatActivity {
    EditText titleEditText, authorEditText, isbnEditText, publishedDateEditText, quantityEditText;
    Button saveButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        titleEditText = findViewById(R.id.title);
        authorEditText = findViewById(R.id.author);
        isbnEditText = findViewById(R.id.isbn);
        publishedDateEditText = findViewById(R.id.published_date);
        quantityEditText = findViewById(R.id.quantity);


        publishedDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> saveBook());
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the date selected listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Format the date and set it to the EditText
                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                publishedDateEditText.setText(selectedDate);
            }
        }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }


    private void saveBook() {
        // Retrieve data from EditText fields
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String isbn = isbnEditText.getText().toString().trim();
        String publishedDate = publishedDateEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();

        // Validate input
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publishedDate.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert quantity to integer
        int quantity = Integer.parseInt(quantityStr);

        Map<String, Object> book = new HashMap<>();
        book.put("title", title);
        book.put("author", author);
        book.put("isbn", isbn);
        book.put("publishedDate", publishedDate);
        book.put("quantity", quantity);


        db.collection("books")
                .add(book)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddBook.this, "Book saved successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddBook.this, "Error saving book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Optionally, you can clear the EditText fields after saving
        clearFields();
    }
    private void clearFields() {
        titleEditText.setText("");
        authorEditText.setText("");
        isbnEditText.setText("");
        publishedDateEditText.setText("");
        quantityEditText.setText("");
    }
}