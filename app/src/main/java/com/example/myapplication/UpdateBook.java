package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateBook extends AppCompatActivity {
    private EditText isbnEditText, titleEditText, authorEditText, publishedDateEditText, quantityEditText;
    private Button fetchButton, updateButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        db = FirebaseFirestore.getInstance();

        isbnEditText = findViewById(R.id.isbn);
        titleEditText = findViewById(R.id.title);
        authorEditText = findViewById(R.id.author);
        publishedDateEditText = findViewById(R.id.published_date);
        quantityEditText = findViewById(R.id.quantity);
        fetchButton = findViewById(R.id.button_fetch);
        updateButton = findViewById(R.id.button_update);

        publishedDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchBookDetails();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });
    }

    private void fetchBookDetails() {
        String isbn = isbnEditText.getText().toString().trim();
        if (isbn.isEmpty()) {
            Toast.makeText(this, "Please enter ISBN", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("books")
                .whereEqualTo("isbn", isbn)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        titleEditText.setText(documentSnapshot.getString("title"));
                        authorEditText.setText(documentSnapshot.getString("author"));
                        publishedDateEditText.setText(documentSnapshot.getString("publishedDate"));
                        quantityEditText.setText(String.valueOf(documentSnapshot.getLong("quantity")));
                    } else {
                        Toast.makeText(UpdateBook.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateBook.this, "Failed to fetch book details", Toast.LENGTH_SHORT).show());
    }

    private void updateBook() {
        String isbn = isbnEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();
        String author = authorEditText.getText().toString().trim();
        String publishedDate = publishedDateEditText.getText().toString().trim();
        String quantityStr = quantityEditText.getText().toString().trim();

        if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || publishedDate.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);

        Map<String, Object> book = new HashMap<>();
        book.put("title", title);
        book.put("author", author);
        book.put("isbn", isbn);
        book.put("publishedDate", publishedDate);
        book.put("quantity", quantity);

        db.collection("books")
                .whereEqualTo("isbn", isbn)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = documentSnapshot.getId();
                        db.collection("books").document(documentId)
                                .set(book)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(UpdateBook.this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                })
                                .addOnFailureListener(e -> Toast.makeText(UpdateBook.this, "Failed to update book", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(UpdateBook.this, "Book not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateBook.this, "Failed to update book", Toast.LENGTH_SHORT).show());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                publishedDateEditText.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void clearFields() {
        isbnEditText.setText("");
        titleEditText.setText("");
        authorEditText.setText("");
        publishedDateEditText.setText("");
        quantityEditText.setText("");
    }
}
