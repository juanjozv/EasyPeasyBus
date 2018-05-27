package com.example.toshiba.easypeasybus;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class Planificador extends AppCompatActivity{

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

    private int index = 0;

    private ArrayList<Integer> daysSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planificador);
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
        crear.setOnClickListener(arg0 -> {
            if (isValid()) {
                crearAlertas();
            } else {
                mensajeOK("Debe seleccionar un día y una hora");
            }
        });
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

    private void resetDays(){
        tD.setChecked(false);
        tL.setChecked(false);
        tM.setChecked(false);
        tMi.setChecked(false);
        tJ.setChecked(false);
        tV.setChecked(false);
        tS.setChecked(false);
        etHora.setText("");
    }

    private boolean isValid(){
        return ((tD.isChecked()|| tL.isChecked() || tM.isChecked() || tMi.isChecked() || tJ.isChecked() || tV.isChecked() || tS.isChecked()) && etHora.getText().toString().length() > 0);
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

    private void crearAlertas() {
        getLastAlarmIndex();
        guardarDias();
        int size = daysSelected.size();
        while(size > 0){
            crearAlerta(daysSelected.remove(0));
            size--;
        }
        saveIndex();
        mensajeOK("Se creó un nuevo recordatorio.");
        resetDays();

    }

    private void crearAlerta(int dia) {
        APBAuth vg = APBAuth.getInstance();
        String[] hora = etHora.getText().toString().split(":");
        Calendar firingCal= Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        vg.setHora(etHora.getText().toString());
        vg.setAlarmIndex(index++);
        int hour = Integer.parseInt(hora[0]);
        int minutes = Integer.parseInt(hora[1].substring(0, 2));
        firingCal.set(Calendar.DAY_OF_WEEK, dia);
        firingCal.set(Calendar.HOUR, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, minutes - 5); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        Long intendedTime = firingCal.getTimeInMillis();
        Long actualTime = currentCal.getTimeInMillis();
        //if(intendedTime > actualTime) {
            Intent intent = new Intent(this, Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, vg.getAlarmIndex(), intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.setRepeating(am.RTC_WAKEUP, intendedTime, am.INTERVAL_DAY * 7, pendingIntent);
       // }
    }



    public void getLastAlarmIndex(){

        final int tam_bloque_lectura = 100;
        try
        {
            FileInputStream fIn = openFileInput("index_alerta");
            InputStreamReader isr = new InputStreamReader(fIn);
            char[] inputBuffer = new char[tam_bloque_lectura];
            int charRead;
            if ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                index = Integer.parseInt(readString);
            }
            isr.close();

        }
        catch (IOException ioe) {
            index = 0;
        }
    }

    public void saveIndex(){
        try {
            FileOutputStream fOut = openFileOutput("index_alerta", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(String.valueOf(index));
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
