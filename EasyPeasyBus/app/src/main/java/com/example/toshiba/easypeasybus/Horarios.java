package com.example.toshiba.easypeasybus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Horarios extends AppCompatActivity {

    private FirebaseDatabase mAuth;
    private ArrayList<String> puntosPartida;
    private ArrayList<String> destinos;
    private String de;
    private String hacia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        mAuth = FirebaseDatabase.getInstance("https://easypeasybus.firebaseio.com/");
        puntosPartida = new ArrayList<>();
        cargarPuntosDePartida();

    }

    private void cargarPuntosDePartida() {

        DatabaseReference mRef = mAuth.getReference("Horarios");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String provincia ="";
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    provincia = postSnapshot.getKey();
                    puntosPartida.add(provincia);
                }
                cargarSpinnerPuntosPartida();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void cargarDestinos(){
        destinos = new ArrayList<>();
        DatabaseReference mRef = mAuth.getReference("Horarios").child(de);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String provincia ="";
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    provincia = postSnapshot.getKey();
                    destinos.add(provincia);
                }
                cargarSpinnerDestinos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cargarHorario(){
        DatabaseReference mRef = mAuth.getReference("Horarios").child(de).child(hacia);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HorarioBus horario = dataSnapshot.getValue(HorarioBus.class);
                TextView horarioTv = (TextView) findViewById(R.id.horario);
                String mensaje = "De " + horario.getPrimero() + " a " + horario.getUltimo() + " cada " + String.valueOf(horario.getFrecuencia()) + " minutos.";
                horarioTv.setText(mensaje);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void cargarSpinnerPuntosPartida() {

        //---Spinner View---
        Spinner sProvincias = (Spinner) findViewById(R.id.provincias);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, puntosPartida);

        sProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                de = puntosPartida.get(position);
                cargarDestinos();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sProvincias.setAdapter(adapter);

    }
    private void cargarSpinnerDestinos() {

        //---Spinner View---
        Spinner sProvincias = (Spinner) findViewById(R.id.cantones);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, destinos);

        sProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                hacia = destinos.get(position);
                cargarHorario();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sProvincias.setAdapter(adapter);

    }
}
