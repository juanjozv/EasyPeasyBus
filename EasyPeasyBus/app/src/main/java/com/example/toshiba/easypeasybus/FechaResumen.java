package com.example.toshiba.easypeasybus;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.DatePickerDialog;;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FechaResumen extends Fragment {

    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private FirebaseAuth mAuth;

    //Widgets
    EditText etFecha;
    ImageButton ibObtenerFecha;

    View v1;


    public FechaResumen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fecha_resumen, container, false);
        v1 = view;
        //Widget EditText donde se mostrara la fecha obtenida
        etFecha = (EditText) view.findViewById(R.id.et_mostrar_fecha_picker);
        //Widget ImageButton del cual usaremos el evento clic para obtener la fecha
        ibObtenerFecha = (ImageButton) view.findViewById(R.id.ib_obtener_fecha);

        mAuth = FirebaseAuth.getInstance();

        ibObtenerFecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                obtenerFecha();

            }
        });

        LinearLayout MiLinearLayout = (LinearLayout) view.findViewById(R.id.Ir);
        MiLinearLayout.setOnClickListener(v -> {

            FirebaseUser user = mAuth.getCurrentUser();

            EditText MiEditText = (EditText) view.findViewById(R.id.et_mostrar_fecha_picker);
            String fecha = MiEditText.getText().toString();
            if(!fecha.equals("") && user != null) {
                APBAuth apb = APBAuth.getInstance();
                apb.setDateSelected(fecha);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                ResumenGasto mifrag2 = new ResumenGasto();
                fragmentTransaction.replace(R.id.contenedor, mifrag2, "Indentifier3");
                fragmentTransaction.commit();

            } else {
                if(fecha.equals("")) {
                    MensajeOK("Fecha vacía");
                }else {
                    MensajeOK("Usuario nulo o lo loggeado");
                }
            }

        });
        return view;
    }



    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

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

}
