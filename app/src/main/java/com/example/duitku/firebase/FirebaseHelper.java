package com.example.duitku.firebase;

import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirebaseHelper {

    private static FirebaseFirestore db;
    private static FirebaseAuth mAuth;
    private static FirebaseUser currentUser;

    private DocumentReference user_doc_ref;

    private CollectionReference article_col_ref;
    private CollectionReference budget_col_ref;
    private CollectionReference category_col_ref;
    private CollectionReference transaction_col_ref;
    private CollectionReference wallet_col_ref;
    private CollectionReference user_col_ref;
    private CollectionReference feedback_col_ref;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        setupReferences();
    }

    private void setupReferences() {
        user_doc_ref = db.collection("users").document(currentUser.getUid());

        article_col_ref = user_doc_ref.collection("article");
        budget_col_ref = user_doc_ref.collection("budget");
        category_col_ref = user_doc_ref.collection("category");
        transaction_col_ref = user_doc_ref.collection("transaction");
        wallet_col_ref = user_doc_ref.collection("wallet");
        user_col_ref = user_doc_ref.collection("user");
        feedback_col_ref = db.collection("feedback");
    }

    public void addFeedback(String feedback){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Feedback", feedback);
        feedback_col_ref.document().set(hashMap);
    }

    public void addBudgetToFirebase(HashMap<String, Object> budgetHashMap){
        budget_col_ref.document("" +
                budgetHashMap.get(BudgetEntry.COLUMN_ID)).set(budgetHashMap);
    }

    public void addCategoryToFirebase(HashMap<String, Object> categoryHashMap){
        category_col_ref.document("" +
                categoryHashMap.get(CategoryEntry.COLUMN_ID)).set(categoryHashMap);
    }

    public void addWalletToFirebase(HashMap<String, Object> walletHashMap){
        wallet_col_ref.document("" +
                walletHashMap.get(WalletEntry.COLUMN_ID)).set(walletHashMap);
    }

    public void addTransactionToFirebase(HashMap<String, Object> transactionHashMap){
        transaction_col_ref.document("" +
                transactionHashMap.get(TransactionEntry.COLUMN_ID)).set(transactionHashMap);
    }

    public Task<Void> addUserToFirebase(HashMap<String, Object> userHashMap){
        return user_col_ref.document("" +
                userHashMap.get(UserEntry.COLUMN_ID)).set(userHashMap);
    }

    public CollectionReference getArticleRef(){
        return article_col_ref;
    }

    public CollectionReference getTransactionRef(){
        return transaction_col_ref;
    }

    public CollectionReference getWalletRef(){
        return wallet_col_ref;
    }

    public CollectionReference getBudgetRef(){
        return budget_col_ref;
    }

    public CollectionReference getCategoryRef(){
        return category_col_ref;
    }

    public CollectionReference getUserRef() {
        return user_col_ref;
    }

}