package duitku.project.se.firebase;

import duitku.project.se.article.Article;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseReader {
    // ini buat read semua data dari user yg logged in dari sub collectionnya
    // semuanya ud diconvert balik dari hashmap ke bentuk asli classnya
    // panggilnya kalau butuh backup dari cloud aja

    private FirebaseHelper fbHelper;

    public FirebaseReader() {
        fbHelper = new FirebaseHelper();
    }

    public List<Article> getAllArticle() {
        final List<Article> ret = new ArrayList<>();
        fbHelper.getArticleRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            ret.add(doc.toObject(Article.class));
                        }
                    }
                });
        return ret;
    }

    public void getAllBudgets(OnSuccessListener<QuerySnapshot> listener) {
        fbHelper.getBudgetRef().get().addOnSuccessListener(listener);
    }

    public void getAllCategories(OnSuccessListener<QuerySnapshot> listener) {
        fbHelper.getCategoryRef().get().addOnSuccessListener(listener);
    }

    public void getAllTransactions(OnSuccessListener<QuerySnapshot> listener) {
        fbHelper.getTransactionRef().get().addOnSuccessListener(listener);
    }

    public void getAllWallets(OnSuccessListener<QuerySnapshot> listener) {
        fbHelper.getWalletRef().get().addOnSuccessListener(listener);
    }

    public void getAllArticle(OnSuccessListener<QuerySnapshot> listener){
        fbHelper.getArticleRef().get().addOnSuccessListener(listener);
    }

}