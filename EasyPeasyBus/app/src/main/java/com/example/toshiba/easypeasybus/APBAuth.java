package com.example.toshiba.easypeasybus;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class APBAuth {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private static APBAuth instance = null;

    protected APBAuth() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public static APBAuth getInstance() {
        if(instance == null) {instance = new APBAuth(); }
        return instance;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public void signOut() {
        this.mAuth.signOut();
    }
}
