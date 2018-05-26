package com.example.toshiba.easypeasybus;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResumenGasto extends Fragment {

    View v1;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://easypeasybus.firebaseio.com/");
    DatabaseReference myRef = database.getReference("Gastos");
    private FirebaseAuth mAuth;
    ArrayList<String> gastos = new ArrayList<>();
    HashMap<String, String> info = new HashMap<>();
    Integer totalDayAmount = 0;



    public ResumenGasto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_resumen_gasto, container, false);
        v1 = view;

        mAuth = FirebaseAuth.getInstance();

        DandoClickALosItems();
        obtainUserDailyMounts();

        LinearLayout MiLinearLayout = (LinearLayout) view.findViewById(R.id.total);
        MiLinearLayout.setOnClickListener(v -> {
            showTotalDayAmount(totalDayAmount);
        });


        return view;
    }

    private String generateActionInfo(Action action) {
        return "Día: " + action.getDate() +
                "\nHora: " + action.getHour() + "\nMonto: " + action.getAmount() + " colones.";
    }

    private void createHash(Integer index, String informacion) {
        info.put(index.toString(), informacion);
    }

    private void obtainUserDailyMounts() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String email = formatEmailFirebaseFormat(user.getEmail());
            gastos = new ArrayList<>();
            info = new HashMap<>();
            //totalDayAmount = 0;
            myRef = myRef.child(email);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Action action;
                    Integer index = 1;
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        action = postSnapshot.getValue(Action.class);
                        APBAuth apb = APBAuth.getInstance();
                        if(apb.getDateSelected().equals(action.getDate())) {
                            String nombre = action.getBusName();
                            totalDayAmount += action.getAmount();
                            gastos.add(index + ". " + nombre);
                            String informacion = generateActionInfo(action);
                            createHash(index, informacion);
                            index++;
                        }
                    }
                    LlenarListView();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
        }else {
            Mensaje("Usuario nulo o no loggeado.");
        }

    }

    private void LlenarListView() {
        if(getActivity() != null) {
            ArrayAdapter adaptador = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, gastos);
            ListView milistview = (ListView) v1.findViewById(R.id.list);
            milistview.setAdapter(adaptador);

            if(gastos.size() < 1) {
                MensajeOK("No hay acciones en este día");
            }
        }

    }

    public void DandoClickALosItems() {
        ListView list = (ListView) v1.findViewById(R.id.list);
        list.setOnItemClickListener((paret, viewClicked, position, id) -> {
            TextView textView = (TextView) viewClicked;
            String val = textView.getText().toString();
            String key = val.split("\\.")[0];
            MensajeOK(info.get(key));
        });
    }

    public void MensajeOK(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", (dialog, id) -> {
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void Mensaje(String msg){ Toast.makeText(getActivity(), msg,Toast.LENGTH_SHORT).show();};

    public String formatEmailFirebaseFormat(String email) {
        return email.replace(".", "_");
    }

    public void showTotalDayAmount(Integer amount) {
        TextView Mi_textview = (TextView) v1.findViewById(R.id.gtotal);
        Mi_textview.setText(amount.toString() + " colones.");
    }



}
