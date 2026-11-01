package com.example.librarymangaement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Api.ApiService;
import DataBase.DataBase;
import model_adapter.AdapterBooks;
import model_adapter.BookResponseModel;
import model_adapter.OnClickedListner;
import model_adapter.docs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import DataBase.BooksTable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseBooks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseBooks extends Fragment implements OnClickedListner {

    RecyclerView recyler;
    AdapterBooks adapterBooks;
    List<docs> booksList, originalList;

    ProgressBar progressBar;

    BookForSave BookForSave;

    ImageView imgNetwork;
    TextView txtErromsg;

    MaterialButton retrybtn;

    EditText editText;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof BookForSave) {
            BookForSave = (BookForSave) context;
        } else {
            throw new IllegalStateException(
                    "MainActivity must implement BookForSave"
            );
        }
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowseBooks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseBooks.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseBooks newInstance(String param1, String param2) {
        BrowseBooks fragment = new BrowseBooks();
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
        View view = inflater.inflate(R.layout.fragment_browse_books, container, false);
        booksList = new ArrayList<>();
        originalList = new ArrayList<>();

        recyler = view.findViewById(R.id.recycler);
        txtErromsg = view.findViewById(R.id.tvError);
        imgNetwork = view.findViewById(R.id.imgState);
        retrybtn = view.findViewById(R.id.retry);

        editText = view.findViewById(R.id.etSearch);

        progressBar = view.findViewById(R.id.progressBar);

        adapterCheck(booksList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyler.setLayoutManager(linearLayoutManager);
        recyler.setAdapter(adapterBooks);

        fetchBooks();

        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetOn();
                fetchBooks();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                MaterialToolbar toolbar = requireActivity().findViewById(R.id.tool_bar);
                if(toolbar != null) {
                    if(hasFocus) {
                        toolbar.setVisibility(View.GONE);
                    }else{
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH){
                    String searched = editText.getText().toString().trim();

                    if(!searched.isEmpty()){
                        searchedBooks(searched);
                    }else{
                        Toast.makeText(requireContext(), "Please enter a book name!", Toast.LENGTH_SHORT).show();
                    }
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                    }
                    return true;
                }
                return false;
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, new savedBooks()).commit();

               BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
                if(bottomNavigationView != null){
                    bottomNavigationView.setSelectedItemId(R.id.saveM);
                }
            }
        });

        return view;
    }

    public void adapterCheck(List<docs> modells){
        if(adapterBooks == null){
            adapterBooks = new AdapterBooks(modells, (OnClickedListner) this);
        }else{
            adapterBooks.notifyDataSetChanged();
        }
    }
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService api = retrofit.create(ApiService.class);


    public void fetchBooks(){
        progressBar.setVisibility(getView().VISIBLE);
        Call call = api.getBooks();

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful() && response.body() != null){
                    BookResponseModel responseModel = (BookResponseModel) response.body();
                    List<docs> models = responseModel.getBooks();
                    if(models == null){
                        Log.d("API_BOOKS", "List is empty");
                        return;
                    }
                    booksList.addAll(models);
                    originalList.addAll(booksList);
                    progressBar.setVisibility(getView().GONE);
                    adapterBooks.notifyDataSetChanged();
                }else{
                    Toast.makeText(requireContext(), "error: "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
                progressBar.setVisibility(getView().GONE);
                internetOff();
            }
        });
    }


    @Override
    public void onSaved(docs doc) {
        String author2= "",author1="";
        List<String> authors = doc.getAuthor();

        if (authors != null && authors.size() > 0
                && authors.get(0) != null) {
            author1 = authors.get(0);
        }

        if (authors != null && authors.size() > 1
                && authors.get(1) != null) {
            author2 = authors.get(1);
        }


        BooksTable book = new BooksTable(doc.getKey(),doc.getTitle(),doc.getCover_i(),doc.getFirst_publish_year(),author1,author2);

        if(BookForSave != null){
            BookForSave.bookToSave(book);
        }else{
            Toast.makeText(requireContext(), "Save connection is not initialized", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void bookPreviewDetails(docs doc) {
        if(BookForSave != null){
            BookForSave.bookDetails(doc);
        }
    }


    private void internetOff(){
        progressBar.setVisibility(getView().GONE);
        imgNetwork.setVisibility(getView().VISIBLE);
        txtErromsg.setVisibility(getView().VISIBLE);
        retrybtn.setVisibility(getView().VISIBLE);
    }

    private void internetOn(){
        imgNetwork.setVisibility(getView().GONE);
        txtErromsg.setVisibility(getView().GONE);
        retrybtn.setVisibility(getView().GONE);
    }

    public void searchedBooks(String searchedBook){
        progressBar.setVisibility(View.VISIBLE);
        Call call = api.getBooksSearch(searchedBook);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                internetOn();
                if(response.isSuccessful() && response.body() != null){
                    BookResponseModel responseModel = (BookResponseModel) response.body();
                    List<docs> searchedlist =  responseModel.getBooks();
                    if(searchedlist.isEmpty()){
                        Toast.makeText(requireContext(), "Couldn't find books", Toast.LENGTH_SHORT).show();
                    }else{
                        booksList.clear();
                        booksList.addAll(searchedlist);
                        progressBar.setVisibility(View.GONE);
                        adapterBooks.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
                internetOn();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}