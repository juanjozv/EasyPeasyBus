package com.example.toshiba.easypeasybus;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngresarGasto extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://easypeasybus.firebaseio.com/");
    DatabaseReference myRef = database.getReference("Gastos");
    private FirebaseAuth mAuth;
    private View v1;


    public IngresarGasto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingresar_gasto, container, false);
        v1 = view;

        mAuth = FirebaseAuth.getInstance();

        LinearLayout MiLinearLayout = (LinearLayout) view.findViewById(R.id.registrarMonto);
        MiLinearLayout.setOnClickListener(v -> {
            EditText monto = (EditText) view.findViewById(R.id.gastoNumber);
            EditText nombre = (EditText) view.findViewById(R.id.nombreBus);
            String amount = monto.getText().toString(), name = nombre.getText().toString();
            if(!amount.equals("") && !name.equals("")) {
                String total = "AutoBus: "+  name + "\nMonto: " + amount + " colones. ";
                MensajeOK(total);
            }else {
                Mensaje("Debe llenar los dos campos.");
            }
        });


        return view;
    }

    public void Mensaje(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

    public void addAction(String name, String amount) {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String email = formatEmailFirebaseFormat(user.getEmail());
            String date = obtainCurrentDate();
            String hour = obtainCurrentHour();
            Action newAction = new Action(email, date, Integer.parseInt(amount), name, hour);
            myRef.child(email).push().setValue(newAction);
        } else {
            Mensaje("Usuario nulo o no loggeado.");
        }
    }

    public String obtainCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String obtainCurrentHour() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date c = Calendar.getInstance().getTime();
        String formattedDate = dateFormat.format(c);
        return formattedDate;
    }

    public void registerUIData() {
        EditText monto = (EditText) v1.findViewById(R.id.gastoNumber);
        EditText nombre = (EditText) v1.findViewById(R.id.nombreBus);
        addAction(nombre.getText().toString(), monto.getText().toString());
        monto.setText("");
        nombre.setText("");
    }

    public void MensajeOK(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", (dialog, id) -> {
            registerUIData();
        });
        builder1.setNegativeButton("Cancelar", (dialog, id) -> {});
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    //No olvidar hacer el proceso inverso para el otro fragment.
    public String formatEmailFirebaseFormat(String email) {
        return email.replace(".", "_");
    }



}
