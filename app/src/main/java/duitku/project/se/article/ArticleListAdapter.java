package duitku.project.se.article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import duitku.project.se.R;

import java.util.ArrayList;

public class ArticleListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Article> listArticle;
    private LayoutInflater inflater;

    public ArticleListAdapter(Context applicationContext, ArrayList<Article> listArticle) {
        this.context = applicationContext;
        this.listArticle = listArticle;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.listArticle.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_list_article, null);

        TextView title = (TextView) view.findViewById(R.id.article_Title);
        title.setText(listArticle.get(i).getTitle());

        return view;
    }

}