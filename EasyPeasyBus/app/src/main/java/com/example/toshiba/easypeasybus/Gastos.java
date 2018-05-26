package com.example.toshiba.easypeasybus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Gastos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        OnclickDelLinearLayout(R.id.ingresar);
        OnclickDelLinearLayout(R.id.resume);
    }

    public void OnclickDelLinearLayout(int ref) {
        View view = findViewById(ref);
        LinearLayout miLinearLayout = (LinearLayout) view;
        miLinearLayout.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.ingresar:
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    IngresarGasto mifrag = new IngresarGasto();
                    fragmentTransaction.replace(R.id.contenedor, mifrag, "Indentifier1");
                    fragmentTransaction.commit();
                    break;
                case R.id.resume:
                    fm = getFragmentManager();
                    fragmentTransaction = fm.beginTransaction();
                    ResumenGasto mifrag2 = new ResumenGasto();
                    fragmentTransaction.replace(R.id.contenedor, mifrag2, "Indentifier2");
                    fragmentTransaction.commit();
                    break;
                default:break;
            }
        });
    }

    public void Mensaje(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
