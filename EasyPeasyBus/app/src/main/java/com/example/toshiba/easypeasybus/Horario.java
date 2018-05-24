package com.example.toshiba.easypeasybus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Horario extends AppCompatActivity {

    private FirebaseDatabase mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        mAuth = FirebaseDatabase.getInstance("https://easypeasybus.firebaseio.com/");
        cargarSpinnerProvincias();
    }

    private ArrayList<String> getListaProvincias() {
        ArrayList<String> provincias = new ArrayList<>();
        DatabaseReference mRef = mAuth.getReference("Horarios");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String provincia ="";
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    provincia = postSnapshot.getKey();
                    provincias.add(provincia);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return provincias;
    }
    private void cargarSpinnerProvincias() {
        ArrayList<String> provincias = getListaProvincias();

        //---Spinner View---
        Spinner sProvincias = (Spinner) findViewById(R.id.provincias);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, provincias);




        sProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
               // Mensaje(presidents[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sProvincias.setAdapter(adapter);


    }// fin de CargarSpinner
}
