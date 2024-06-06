package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonAddBook,buttonUpdateBook,buttonGetBooks,buttonDeleteBook,buttonSearchBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);


         buttonAddBook = findViewById(R.id.button_add_book);
         buttonUpdateBook = findViewById(R.id.button_update_book);
         buttonGetBooks = findViewById(R.id.button_get_all_books);
         buttonDeleteBook = findViewById(R.id.button_delete_book);
         buttonSearchBook = findViewById(R.id.button_search_book);
        buttonAddBook.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), AddBook.class);
            view.getContext().startActivity(intent);
        });

        buttonUpdateBook.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UpdateBook.class);
            view.getContext().startActivity(intent);
        });

        buttonGetBooks.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GetAllBooks.class);
            view.getContext().startActivity(intent);
        });

        buttonDeleteBook.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DeleteBook.class);
            view.getContext().startActivity(intent);
        });

        buttonSearchBook.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SearchBook.class);
            view.getContext().startActivity(intent);
        });


    }

}