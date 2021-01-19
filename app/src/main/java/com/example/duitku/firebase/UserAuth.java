package com.example.duitku.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class UserAuth {

    private FirebaseAuth mAuth;

    public String username;
    public String email;

    private static final UserAuth instance = new UserAuth();

    private UserAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signIn() {

    }

    public void signOut() {

    }
}
