package com.example.duitku.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.duitku.R;
import com.example.duitku.firebase.FirebaseReader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ArticleFragment extends Fragment {

    private ArrayList<Article> listArticle = new ArrayList<>();
    private View rootView;
    private ArticleListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_article, container, false);

        listArticle.clear();
        fillArticles();

        return rootView;
    }

    public void fillArticles() {
        FirebaseReader firebaseReader = new FirebaseReader();
        firebaseReader.getAllArticle(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    listArticle.add(doc.toObject(Article.class));
                }
                populateArticles();
            }
        });
    }

    private void populateArticles(){
        ListView listView = rootView.findViewById(R.id.list_article);
        adapter = new ArticleListAdapter(getContext(), listArticle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strURL = listArticle.get(position).getAddress();
                Intent implicit = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(strURL));
                String title = "Select a browser";
                Intent chooser = Intent.createChooser(implicit, title);
//                if (implicit.resolveActivity(get)!= null) {
                startActivity(chooser);
//                }
            }
        });
    }

}