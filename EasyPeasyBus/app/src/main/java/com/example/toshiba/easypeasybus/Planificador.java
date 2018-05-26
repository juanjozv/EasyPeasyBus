package com.example.toshiba.easypeasybus;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Planificador extends AppCompatActivity{

    private FirebaseDatabase mAuth;
    private FirebaseAuth mUs;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    EditText etHora;
    ImageButton ibObtenerHora;

    //Day buttons
    ToggleButton tD;
    ToggleButton tL;
    ToggleButton tM;
    ToggleButton tMi;
    ToggleButton tJ;
    ToggleButton tV;
    ToggleButton tS;

    private ArrayList<Integer> daysSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planificador);
        mUs = FirebaseAuth.getInstance();
        mAuth = FirebaseDatabase.getInstance("https://easypeasybus.firebaseio.com/");
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la hora
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);
        ibObtenerHora.setOnClickListener(arg0 -> obtenerHora());
        tD = (ToggleButton) findViewById(R.id.tD);
        tL = (ToggleButton) findViewById(R.id.tL);
        tM = (ToggleButton) findViewById(R.id.tM);
        tMi = (ToggleButton) findViewById(R.id.tMi);
        tJ = (ToggleButton) findViewById(R.id.tJ);
        tV = (ToggleButton) findViewById(R.id.tV);
        tS = (ToggleButton) findViewById(R.id.tS);

        CardView crear = (CardView) findViewById(R.id.agregar_alerta);
        crear.setOnClickListener(arg0 -> guardarAlertas());

    }



    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, (TimePicker view, int hourOfDay, int minute) -> {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM = hourOfDay < 12 ? "a.m." : "p.m.";
                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
        }, hora, minuto, false);

        recogerHora.show();
    }

    private void guardarDias() {
        if(tD.isChecked()){
            daysSelected.add(Calendar.SUNDAY);
        }
        if(tL.isChecked()){
            daysSelected.add(Calendar.MONDAY);
        }
        if(tM.isChecked()){
            daysSelected.add(Calendar.TUESDAY);
        }
        if(tMi.isChecked()){
            daysSelected.add(Calendar.WEDNESDAY);
        }
        if(tJ.isChecked()){
            daysSelected.add(Calendar.THURSDAY);
        }
        if(tV.isChecked()){
            daysSelected.add(Calendar.FRIDAY);
        }
        if(tS.isChecked()){
            daysSelected.add(Calendar.SATURDAY);
        }
    }

    private void guardarAlertas() {
        guardarDias();
        int size = daysSelected.size();
        while(size > 0){
           // guardarAlerta(daysSelected.remove(0));
            size--;
        }
        mensajeOK("Se creó un nuevo recordatorio.");
    }

    private void guardarAlerta(String dia) {
        FirebaseUser user = mUs.getCurrentUser();
        String email = formatEmailFirebaseFormat(user.getEmail());
        String hora = etHora.getText().toString();
        DatabaseReference mRef = mAuth.getReference("Planificador").child(dia).child(email);
        Alerta newAlerta = new Alerta(hora);
        mRef.push().setValue(newAlerta);
    }

    public String formatEmailFirebaseFormat(String email) {
        return email.replace(".", "_");
    }

    public void mensajeOK(String msg){
        View v1 = getWindow().getDecorView().getRootView();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(v1.getContext());
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK", (DialogInterface dialog, int id) -> {});
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
