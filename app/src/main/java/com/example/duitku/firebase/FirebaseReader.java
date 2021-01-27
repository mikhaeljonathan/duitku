package com.example.duitku.firebase;

import com.example.duitku.article.Article;
import com.example.duitku.budget.Budget;
import com.example.duitku.category.Category;
import com.example.duitku.user.User;
import com.example.duitku.wallet.Wallet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FirebaseReader {
    // ini buat read semua data dari user yg logged in dari sub collectionnya
    // semuanya ud diconvert balik dari hashmap ke bentuk asli classnya
    // panggilnya kalau butuh backup dari cloud aja

    private FirebaseHelper fbHelper;
    private User user;
    private List<Budget> budgetList;
    private List<Category> categoryList;
    private List<Transaction> transactionList;
    private List<Wallet> walletList;

    private User user;

    public FirebaseReader (){
        fbHelper = new FirebaseHelper();
        getAll();
    }

    public User getUserFromFirestore() {
        fbHelper.getUserRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        user = queryDocumentSnapshots.toObjects(User.class).get(0);
                    }
                });
        return user;
    }

    public List<Article> getAllArticle() {
        final List<Article> ret = new ArrayList<>();
        fbHelper.getArticleRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            ret.add(doc.toObject(Article.class));
                        }
                    }
                });
        return ret;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public List<Wallet> getWalletList() {
        return walletList;
    }

    private void getAllBudgets() {
        fbHelper.getBudgetRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            budgetList.add(doc.toObject(Budget.class));
                        }
                    }
                });
    }

    private void getAllCategories() {
        fbHelper.getCategoryRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            categoryList.add(doc.toObject(Category.class));
                        }
                    }
                });
    }

    private void getAllTransactions() {
        fbHelper.getTransactionRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            transactionList.add(doc.toObject(Transaction.class));
                        }
                    }
                });
    }

    private void getAllWallets() {
        fbHelper.getWalletRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            walletList.add(doc.toObject(Wallet.class));
                        }
                    }
                });
    }

    private void getAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAllBudgets();
                getAllCategories();
                getAllTransactions();
                getAllWallets();
            }
        }).start();
    }
}