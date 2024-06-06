package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class DeleteBookAdapter extends RecyclerView.Adapter<DeleteBookAdapter.ViewHolder> {
    private List<Book> bookList;
    private List<Book> originalBookList;
    private Context context;
    private FirebaseFirestore db;

    public DeleteBookAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.originalBookList = bookList; // Save original list for filtering
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_with_delete, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Book book = bookList.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(book.getIsbn());
            }
        });
    }

    private void deleteBook(String isbn) {
        db.collection("books")
                .whereEqualTo("isbn", isbn)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                                    // Update RecyclerView dataset after deletion
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        bookList.removeIf(book -> book.getIsbn().equals(isbn));
                                    }
                                    notifyDataSetChanged(); // Notify adapter of dataset change
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to delete book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, authorTextView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Method to filter book list by availability
    public void filterAvailableBooks() {
        bookList.clear();
        for (Book book : originalBookList) {
            if (book.getQuantity() > 0) {
                bookList.add(book);
            }
        }
        notifyDataSetChanged(); // Notify adapter of dataset change
    }
}