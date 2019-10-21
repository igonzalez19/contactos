package com.example.aadpractica1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.aadpractica1.settings.SettingsActivity;

/*INSTRUCCIONES DE LA PRACTICA
1. solo dos espacios de almacenamiento interno y privado
2. se trabaje con archivos, preferencias compartidas y settings
    SETTING: opcion si quiero recordar el nombre del archivo guardado y otra opcion del sitio donde lo hemos guardado (interno o privado)
    y algun contenido mas extra
3. mejorar diseño de la interfaz (por ejemplo cuando vas a leer sobra algun recuedro, mejorar eso)
4. guardar contactos en la llocalizacion y archivo que el diga (csv)
5. opcion de reconocer historico de los archivos guardados (array con spinner en modo grafico)
6. desaparece el escribir archivo antiguo

*/
public class MainActivity extends AppCompatActivity {

    //recordar archivo
    static TextView ultimoArchivo;
    static Spinner spinner;


    private Button btLeer, btEscribir;
    private Intent escribir, leer;
    private static final String KEY_ARCHIVO = "archivo";
    private static final String KEY_TIPO = "tipo";
    private static final String TAG = "zyaEscribir";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        accionBotones();
        readSettings();

    }

    private void accionBotones() {

        btEscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                escribir = new Intent(MainActivity.this, EscribirActivity.class);
                startActivity(escribir);
            }
        });

        btLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leer = new Intent(MainActivity.this, LeerActivity.class);
                startActivity(leer);
            }
        });
    }


    private void readSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String storage = sharedPreferences.getString("AlmacenamientoSet", "intern");

        Log.v(TAG, storage);
    }


    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnSettings:

                showSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initComponents() {

        spinner = findViewById(R.id.historial);
        ultimoArchivo = findViewById(R.id.tvUltimoArchivo);
        btLeer = findViewById(R.id.btLeer);
        btEscribir = findViewById(R.id.btEscribir);
    }


}
