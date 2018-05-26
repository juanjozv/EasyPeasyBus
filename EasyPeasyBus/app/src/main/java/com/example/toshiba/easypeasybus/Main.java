package com.example.toshiba.easypeasybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CardViews
        OnclickDelCardView(R.id.cardViewMapa);
        OnclickDelCardView(R.id.cardViewPlanificador);
        OnclickDelCardView(R.id.cardViewGastos);
        OnclickDelCardView(R.id.cardViewHorarios);


    }

    public void Mensaje(String msg){Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();}



    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            TextView name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navName);
            name.setText(currentUser.getDisplayName());
            TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navEmail);
            email.setText(currentUser.getEmail());
            Uri photoUrl = currentUser.getPhotoUrl();
            if(photoUrl != null) {
                ImageView Mi_imageview = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView2);
                Glide
                        .with(getApplicationContext())
                        .load(photoUrl) // the uri you got from Firebase
                        .centerCrop()
                        .into(Mi_imageview); //Your imageView variable
            }else {
                ImageView Mi_imageview = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView2);
                Mi_imageview.setImageResource(R.drawable.ic_account_circle_white_100dp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.iniciarSesion) {
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        } else if (id == R.id.video) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse( "https://youtu.be/WEUmaALyPRM" ));
            startActivity(i);

        } else if (id == R.id.autores) {
            Intent intento = new Intent(getApplicationContext(), Info.class);
            startActivity(intento);

        } else if (id == R.id.cerrarSesion) {
            mAuth.signOut();
            TextView name = (TextView) findViewById(R.id.navName);
            name.setText(R.string.nav_header_title);
            TextView email = (TextView) findViewById(R.id.navEmail);
            email.setText(R.string.nav_header_subtitle);
            ImageView Mi_imageview = (ImageView) findViewById(R.id.imageView2);
            Mi_imageview.setImageResource(R.drawable.ic_account_circle_white_100dp);
            Mensaje("Se ha cerrado la sesión");

        } else if (id == R.id.crearCuenta) {
            APBAuth auth = APBAuth.getInstance();
            auth.setCreatingAccount(true);
            Intent intento = new Intent(getApplicationContext(), Login.class);
            startActivity(intento);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void OnclickDelCardView(int ref) {
        View view =findViewById(ref);
        CardView miCardView = (CardView) view;
        miCardView.setOnClickListener( v -> {
            Intent intento;
                switch (v.getId()) {
                    case R.id.cardViewMapa:
                        intento = new Intent(getApplicationContext(), Maps.class);
                        startActivity(intento);
                        break;

                    case R.id.cardViewPlanificador:
                        intento = new Intent(getApplicationContext(), Planificador.class);
                        startActivity(intento);
                        break;

                    case R.id.cardViewGastos:


                        break;

                    case R.id.cardViewHorarios:
                        intento = new Intent(getApplicationContext(), Horarios.class);
                        startActivity(intento);
                        break;
                    default:
                        break;
                }
        });
    }
}
