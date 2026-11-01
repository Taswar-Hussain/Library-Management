package model_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.librarymangaement.BookForSave;
import com.example.librarymangaement.R;

import java.util.ArrayList;
import java.util.List;

import DataBase.BooksTable;

public class SavedBooksAdapter extends RecyclerView.Adapter<SavedBooksAdapter.ViewHolder> {
    List<BooksTable> booksTableList;
    BookForSave onClicked;


    public SavedBooksAdapter(List<BooksTable> booksTableList, BookForSave onClicked) {
        this.booksTableList = booksTableList;
        this.onClicked = onClicked;
    }


    @NonNull
    @Override
    public SavedBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_saved_cards,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedBooksAdapter.ViewHolder holder, int position) {
            BooksTable book  = booksTableList.get(position);

            holder.title.setText(book.getTitle());
            String author1 = book.getAuthor1();
            String author2 = book.getAuthor2();

            if(author1 != null && !author1.isEmpty()){
                if(author2 != null && !author2.isEmpty()){
                    holder.author.setText(author1+","+author2);
                }else{
                    holder.author.setText(author1);
                }
            }else{
                holder.author.setText("Unknown Authors");
            }



            int cover_id = book.getCover_i();

            String URL = "https://covers.openlibrary.org/b/id/"+cover_id+"-M.jpg";


            Glide.with(holder.itemView.getContext())
                .load(URL)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.covers);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onClicked != null){
                        onClicked.bookToDelete(book);
                    }
                    return true;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClicked != null){
                        onClicked.bookDetails(book);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return booksTableList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,author;
        ImageView covers;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_S);
            author = itemView.findViewById(R.id.author_S);
            covers = itemView.findViewById(R.id.cover_s);
        }
    }
}
