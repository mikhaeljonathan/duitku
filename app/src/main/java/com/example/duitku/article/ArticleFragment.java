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

import java.util.ArrayList;

public class ArticleFragment extends Fragment {

    private ArrayList<Article> listArticle = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article,container, false);
        ArticleListAdapter adapter;
        listArticle.clear();
        fillArticles();
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
        return rootView;
    }

    public void fillArticles(){
        listArticle.add(new Article(-1, "4 Cara Terbaik Menyimpan Uang untuk Anda yang Boros", "https://koinworks.com/blog/menyimpan-uang/"));
        listArticle.add(new Article(-1, "Cara Menabung yang Benar menurut Pakar Keuangan", "https://www.cimbniaga.co.id/id/inspirasi/perencanaan/cara-menabung-yang-benar-menurut-pakar-keuangan"));
        listArticle.add(new Article(-1, "Dompet Jebol? Ini Cara Menghemat Uang Agar Tidak Boros", "https://pintek.id/blog/cara-menghemat-uang/"));
        listArticle.add(new Article(-1, "Cara Mengatur Keuangan Agar Tidak Boros, Yuk Hemat!", "https://www.finansialku.com/cara-menghemat-uang-agar-tidak-boros/"));
        listArticle.add(new Article(-1, "8 Cara Cermat Mengatur Gaji Bulanan", "https://www.cermati.com/artikel/8-cara-cermat-mengatur-gaji-bulanan"));
    }

}