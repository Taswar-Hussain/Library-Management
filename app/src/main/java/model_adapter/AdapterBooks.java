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
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AdapterBooks extends RecyclerView.Adapter<AdapterBooks.ViewHolder> {

    OnClickedListner onClickedListner;
    List<docs> docsList;

    public AdapterBooks(List<docs> docsList, OnClickedListner onClickedListner) {
        this.docsList = docsList;
        this.onClickedListner = onClickedListner;
    }

    @NonNull
    @Override
    public AdapterBooks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardbook,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooks.ViewHolder holder, int position) {
            docs model = docsList.get(position);

            holder.title.setText("Title: "+model.getTitle());
            holder.author.setText("Author: "+model.getAuthor());

            String imageUrl = "https://covers.openlibrary.org/b/id/" +model.getCover_i() + "-M.jpg";

            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.cover);

            holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickedListner != null){
                    onClickedListner.onSaved(model);
                }
            }
        });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickedListner != null){
                        onClickedListner.bookPreviewDetails(model);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return docsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,author;
        ImageView cover;
        MaterialButton save;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_c);
            author = itemView.findViewById(R.id.author_c);

            cover = itemView.findViewById(R.id.cover);

            save = itemView.findViewById(R.id.saveC);
        }
    }
}
