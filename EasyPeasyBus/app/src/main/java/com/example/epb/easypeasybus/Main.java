package com.example.epb.easypeasybus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CardViews
        OnclickDelCardView(R.id.cardViewMapa);
        OnclickDelCardView(R.id.cardViewPlanificador);
        OnclickDelCardView(R.id.cardViewGastos);
        OnclickDelCardView(R.id.cardViewHorarios);
    }

    public void OnclickDelCardView(int ref) {
        View view =findViewById(ref);
        CardView miCardView = (CardView) view;
        miCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cardViewMapa:
                        Intent mapIntent = new Intent(getApplicationContext(), Maps.class);
                        startActivity(mapIntent);
                        break;

                    case R.id.cardViewPlanificador:
                        String url = "http://maps.google.com/maps?saddr="+9.996557+","+-84.085443+"&daddr="+9.984507+","+-84.086754+"&mode=transit";
                        //"http://maps.google.com/maps?saddr="+ starting_point_lat+","+starting_point_long+"&daddr="+dest_point_lat+","+dest_point_long+"&mode=transit"

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(url));
                        startActivity(intent);

                        break;

                    case R.id.cardViewGastos:


                        break;

                    case R.id.cardViewHorarios:

                        break;
                    default:break;
                }
            }
        });
    }
}
