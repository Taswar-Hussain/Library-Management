package com.example.librarymangaement;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DataBase.BooksTable;
import DataBase.DataBase;
import model_adapter.SavedBooksAdapter;
import model_adapter.docs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link savedBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class savedBooks extends Fragment implements ListAdapterSaved{

    RecyclerView recyclerView;
    SavedBooksAdapter adapterSaved;
    ArrayList<BooksTable> booksList, originalList;

    ExecutorService executor;

    BookForSave bookToSave;

    ImageView imgstate;
    TextView txtState;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public savedBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment savedBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static savedBooks newInstance(String param1, String param2) {
        savedBooks fragment = new savedBooks();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_books, container, false);

        bookToSave = (BookForSave) requireContext();
        ((MainActivity) requireActivity()).listAdapterSaved = this;

        recyclerView = view.findViewById(R.id.recycler_Save);
        booksList = new ArrayList<>();
        originalList = new ArrayList<>();
        imgstate = view.findViewById(R.id.img_empty);
        txtState = view.findViewById(R.id.nodatatxt);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapterCheck(booksList);
        recyclerView.setAdapter(adapterSaved);

        executor = Executors.newSingleThreadExecutor();

        getAllBooks();


        return view;
    }

    public void adapterCheck(ArrayList<BooksTable> books){
        if(adapterSaved == null){
            adapterSaved = new SavedBooksAdapter(booksList,bookToSave);
        }else{
            adapterSaved.notifyDataSetChanged();
        }
    }

    public void getAllBooks(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<BooksTable> books = DataBase.getInstance(getContext().getApplicationContext()).booksDao().getAllBooks();
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        booksList.clear();
                        booksList.addAll(books);
                        originalList.addAll(booksList);
                        adapterSaved.notifyDataSetChanged();
                        if(booksList.isEmpty()){
                            imgstate.setVisibility(View.VISIBLE);
                            txtState.setVisibility(View.VISIBLE);
                        }else{
                            imgstate.setVisibility(View.GONE);
                            txtState.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    @Override
    public void bookToShowInadapterList(BooksTable booksTable) {
            booksList.add(booksTable);
            adapterSaved.notifyDataSetChanged();
        if (isAdded() && getContext() != null) {
            Toast.makeText(requireContext(), "Book Saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void DeletedBook(BooksTable book) {

        int position = -1;

        for(int i = 0;i<booksList.size();i++){
            if(booksList.get(i).getId() == book.getId()){
                position = i;
                break;
            }
        }

        if(position != -1) {
            booksList.remove(position);
            adapterSaved.notifyItemRemoved(position);
            Toast.makeText(requireContext(), "Book Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllBooks();
    }

    public void clearBooksList(){
        booksList.clear();
        adapterSaved.notifyDataSetChanged();
        imgstate.setVisibility(View.VISIBLE);
        txtState.setVisibility(View.VISIBLE);
    }
}