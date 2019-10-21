package com.example.aadpractica1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.example.aadpractica1.contacto.Contacto;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeerActivity extends AppCompatActivity {

    private static final int ID_PERMISO_LEER = 4;
    private static final String TAG = "leerCon" + MainActivity.class.getName();

    private List<Contacto> contactos;
    TextView listaContactos;
    Button guardar, cargarLista;
    boolean listaCargada=false;
    File ruta;
    String tipoAlmacenamiento;

    //toast
    Context context;
    CharSequence text = "toast por defecto";
    int duration = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leer);

        initComponents();
        accionBotones();
        readSettings();


    }//CIERRE onCreate

    private void getLista() {
        contactos = getListaContactos();//array de contactos
        listaContactos.setText("    "+"CONTACTOS: \n"+contactos.toString());//to.string del array de contactos
        listaCargada = true;
    }


    private void accionBotones() {

        cargarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions(Manifest.permission.READ_CONTACTS,
                        R.string.tituloExplicacion2, R.string.mensajeExplicacion2);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarCSV();
            }
        });

    }

    private void readSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String storage = sharedPreferences.getString("AlmacenamientoSet", "interno");
        boolean guardar = sharedPreferences.getBoolean("guardarArchivo", true);
        tipoAlmacenamiento = storage;
    }

    private void guardarCSV() {

        if (listaCargada){

            if (tipoAlmacenamiento.equals("interno")){
                ruta = LeerActivity.this.getFilesDir();

            } else {
                ruta = LeerActivity.this.getExternalFilesDir(null);
            }


            File f = new File(ruta, "contactos.csv");
            Log.v(TAG, f.getAbsolutePath());
            try {
                FileWriter fw = new FileWriter(f);
                fw.write(getListaContactos().toString());
                fw.flush();
                fw.close();
                text= "Se han guardadados sus contactos en la siguiente ruta \n"+f.getAbsolutePath();
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                toast.show();
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG);
                toast.show();
            }

        } else {
             text = "Primero debe de cargar la lista de contactos para despues guardarla.";
             Toast toast2 = Toast.makeText(context, text, duration);
             toast2.show();
        }
    }

    private void initComponents() {
        context = getApplicationContext();
        listaContactos = findViewById(R.id.tvLista);
        guardar = findViewById(R.id.btGuardar);
        cargarLista = findViewById(R.id.btCargar);
    }

    //PERMISO DE LECTURA
    private void checkPermissions(String permiso, int titulo, int mensaje) {
        if (ContextCompat.checkSelfPermission(this, permiso)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permiso)) {
                explain(R.string.tituloExplicacion, R.string.mensajeExplicacion, permiso);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permiso},
                        ID_PERMISO_LEER);
            }
        } else {
            getLista();
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ID_PERMISO_LEER) {
            //PackageManager.PERMISSION_DENIED;
            //PackageManager.PERMISSION_GRANTED;
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writeNotes();
            }
        }
    }*/


    private void explain(int title, int message, final String permissions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.respSi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(LeerActivity.this, new String[]{permissions}, ID_PERMISO_LEER);
            }
        });
        builder.setNegativeButton(R.string.respNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    public List<Contacto> getListaContactos(){
        int id=1;
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        //int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        List<Contacto> lista = new ArrayList<>();
        Contacto contacto;
        while(cursor.moveToNext()){
            contacto = new Contacto();
            contacto.setId(id);
            contacto.setNombre(cursor.getString(indiceNombre));
            //contacto.setId(1).setNombre("2");
            lista.add(contacto);
            id++;
        }
        return lista;
    }
}
