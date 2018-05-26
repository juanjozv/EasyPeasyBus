package com.example.toshiba.easypeasybus;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class APBAuth {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean isCreatingAccount;
    private String dateSelected;
    private String hora;
    private int alarmIndex;

    private static APBAuth instance = null;

    protected APBAuth() {
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.isCreatingAccount = false;
        this.dateSelected = "";
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

    public Boolean getCreatingAccount() {
        return isCreatingAccount;
    }

    public void setCreatingAccount(Boolean creatingAccount) {
        isCreatingAccount = creatingAccount;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public static void setInstance(APBAuth instance) {
        APBAuth.instance = instance;
    }

    public int getAlarmIndex() {
        return alarmIndex;
    }

    public void setAlarmIndex(int alarmIndex) {
        this.alarmIndex = alarmIndex;
    }
}
