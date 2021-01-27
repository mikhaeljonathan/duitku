package com.example.duitku.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.duitku.R;
import com.example.duitku.budget.Budget;
import com.example.duitku.budget.BudgetController;
import com.example.duitku.category.Category;
import com.example.duitku.category.CategoryController;
import com.example.duitku.database.DuitkuContract.TransactionEntry;
import com.example.duitku.database.DuitkuContract.WalletEntry;
import com.example.duitku.database.DuitkuContract.BudgetEntry;
import com.example.duitku.database.DuitkuContract.CategoryEntry;
import com.example.duitku.database.DuitkuContract.UserEntry;
import com.example.duitku.main.MainActivity;
import com.example.duitku.transaction.Transaction;
import com.example.duitku.transaction.TransactionController;
import com.example.duitku.user.User;
import com.example.duitku.user.UserController;
import com.example.duitku.wallet.Wallet;
import com.example.duitku.wallet.WalletController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GetStarted extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient googleSignInClient;
    private User user;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        Button btn_continue = findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate();
            }
        });
    }

    private void authenticate() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {

                progressDialog.setMessage("Starting signing in...");
                progressDialog.show();
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());

                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    progressDialog.dismiss();
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                checkIfUserExistsInFirestore();
                            } else {
                                Toast.makeText(GetStarted.this, "Error creating user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void checkIfUserExistsInFirestore() {
        new FirebaseHelper().getUserRef().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            user = null; // gaada di firebase
                            createUserInFirestore(firebaseUser);
                        } else { //ada di firebase
                            user = queryDocumentSnapshots.toObjects(User.class).get(0);
                            new UserController(GetStarted.this).addUser(user);
                            getDataFromFirebase();
                            startActivity(new Intent(GetStarted.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void getDataFromFirebase() {
        FirebaseReader firebaseReader = new FirebaseReader();

        final TransactionController transactionController = new TransactionController(this);
        firebaseReader.getAllTransactions(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    getContentResolver().insert(TransactionEntry.CONTENT_URI,
                            transactionController.convertTransactionToContentValues(doc.toObject(Transaction.class)));
                }
            }
        });

        final WalletController walletController = new WalletController(this);
        firebaseReader.getAllWallets(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    getContentResolver().insert(WalletEntry.CONTENT_URI,
                            walletController.convertWalletToContentValues(doc.toObject(Wallet.class)));
                }
            }
        });

        final BudgetController budgetController = new BudgetController(this);
        firebaseReader.getAllBudgets(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    getContentResolver().insert(BudgetEntry.CONTENT_URI,
                            budgetController.convertBudgetToContentValues(doc.toObject(Budget.class)));
                }
            }
        });

        final CategoryController categoryController = new CategoryController(this);
        firebaseReader.getAllCategories(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    getContentResolver().insert(CategoryEntry.CONTENT_URI,
                            categoryController.convertCategoryToContentValues(doc.toObject(Category.class)));
                }
            }
        });

    }

    private User createNewUser(FirebaseUser user) {
        return new User(user.getUid(), user.getDisplayName(), user.getEmail(),
                UserEntry.TYPE_REGULAR, UserEntry.TYPE_FIRST_TIME, null);
    }

    private void createUserInFirestore(FirebaseUser firebaseUser) {
        user = createNewUser(firebaseUser);
        UserController userController = new UserController(this);
        new FirebaseHelper().addUserToFirebase(userController.convertUserToHashMap(user)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new UserController(GetStarted.this).addUser(user);
                startActivity(new Intent(GetStarted.this, MainActivity.class));
                finish();
            }
        });
    }

}