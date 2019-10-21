package com.example.aadpractica1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EscribirActivity extends AppCompatActivity {

    List<String> historial = new ArrayList<>();
    private boolean booleanUltimo;
    private Button btGuardar;
    private EditText etNombre, etValor;
    TextView tvUltimo;
    private int type;
    private String name, value, tipoAlmacenamiento;
    static String ultimoArchivo = "";


    private static final String KEY_ARCHIVO = "archivo";
    private static final String KEY_TIPO = "tipo";
    private static final String TAG = "escribir" + MainActivity.class.getName();
    private static final int NONE = -1;
    private static final int INTERN = 0; // radio id: interno
    private static final int PRIVATE = 1;


    //ajustes toast
    Context context;
    CharSequence text = "toast por defecto";
    int duration = Toast.LENGTH_LONG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escribir);

        initComponents();
        accionBotones();
        readSettings();
    }

    private void accionBotones() {

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile();
            }
        });
    }

    private void setUltimoArchivo(){
            readSettings();
        if (booleanUltimo){

            MainActivity.ultimoArchivo.setText(ultimoArchivo);
            historial.add(ultimoArchivo);
        } else { MainActivity.ultimoArchivo.setText("");}
    }

    public void historial(){

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, historial);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainActivity.spinner.setAdapter(spinnerArrayAdapter);

    }

    private void initComponents() {

        etNombre = findViewById(R.id.etNombre);
        etValor = findViewById(R.id.etValor);
        btGuardar = findViewById(R.id.btCargar);
        context = getApplicationContext();
        tvUltimo = findViewById(R.id.tvUltimoArchivo);
    }


    private static int getCheckedType(String item) { //Le pasamos el radioButton pulsado

        int tipo = NONE;

        if (item.equalsIgnoreCase("interno")) {
            tipo = INTERN;
        } else if (item.equalsIgnoreCase("privado")){
            tipo = PRIVATE;
        }
        return tipo;
    }

    private static File getFile(Context context, int type) {
        File file = null;
        switch (type) {
            case INTERN:
                file = context.getFilesDir();
                break;
            case PRIVATE:
                file = context.getExternalFilesDir(null);
                break;
        }
        return file;
    }

    private File getFile(int type) {

        return EscribirActivity.getFile(EscribirActivity.this, type);
    }

    private boolean isValues() {
        name = etNombre.getText().toString().trim(); // Devuelve el contenido más informacion adicional, trim quita espacios iniciales
        type = getCheckedType(tipoAlmacenamiento); // Obtienes el tipo de almacenamiento de las preferencias
        return !(name.isEmpty() || type == NONE); // Devuelve false si está vacío o si es -1
    }

    private void writeFile() {
        value = etValor.getText().toString().trim();
        if (isValues() && !value.isEmpty()) {
            Toast.makeText(getApplicationContext(), "si ha entrado", Toast.LENGTH_LONG);
                writeNotes();
        }
        else {
            //toast
            text = "ha dejado el campo nombre vacio, introduzca el nombre del archivo y vuelva a probar";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private void writeNotes() {
        File f = new File(getFile(type), name);
        Log.v(TAG, f.getAbsolutePath());
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(value);
            fw.flush();
            fw.close();
            Log.v(TAG, f.getAbsolutePath());
            ultimoArchivo = name;
            setUltimoArchivo();
            historial();
            savePreferrences();

            //toast
            text = "se ha guardado su archivo con exito en la ruta: \n"+f.getAbsolutePath();
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } catch (IOException e) {
            //toast
            Toast toast = Toast.makeText(context, e.getMessage(), duration);
            toast.show();
        }
    }

    private void readSettings() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String storage = sharedPreferences.getString("AlmacenamientoSet", "interno");
        booleanUltimo = sharedPreferences.getBoolean("guardarArchivo", true);

        tipoAlmacenamiento = storage;
        Log.v(TAG, "almacenamiento:"+tipoAlmacenamiento);
    }

    public void savePreferrences(){

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE); //objeto de preferencias compartidas de esta actividad
        SharedPreferences.Editor editor = sharedPref.edit();  //editor de las preferencias compartidas
        editor.putString(KEY_ARCHIVO, name);          //KEY_ARCHIVO:
        editor.putString(KEY_TIPO, String.valueOf(type));
        editor.commit();
    }

}
