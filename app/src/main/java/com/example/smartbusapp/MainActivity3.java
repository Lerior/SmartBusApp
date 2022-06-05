package com.example.smartbusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    Button btnAlert;
    TextView idUser;
    TextInputEditText alumno1, alumno2;
    SharedPreferences sesion;
    String lista[][];
    String numE="1", latV, lonV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        btnAlert=findViewById(R.id.btnAlert);
        idUser=findViewById(R.id.idUser);
        alumno1=findViewById(R.id.alumno1);
        alumno2=findViewById(R.id.alumno2);
        sesion = getSharedPreferences("sesion",0);
        idUser.setText(sesion.getString("user",""));
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { agregar(); }
        });
     latV=getIntent().getStringExtra("dato1");
     lonV=getIntent().getStringExtra("dato2");
        //Toast.makeText(MainActivity3.this, "Latitud " + latV, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity3.this, "Longitud " + lonV, Toast.LENGTH_SHORT).show();
    }
    private void agregar(){
        if (!(alumno1.getText()==null) && !(alumno2.getText()==null)){
            numE="3";
        }else
        if (!(alumno2.getText()==null) && alumno1.getText() ==null){
            numE="2";
        }else
        if (!(alumno1.getText()==null) && alumno2.getText() ==null){
            numE="2";
        }
        String url = Uri.parse(Config.URL + "alert.php")
                .buildUpon().build().toString();
        StringRequest peticion  = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        agregarRespuesta(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Error", error.getMessage());
                Toast.makeText(MainActivity3.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization",
                        sesion.getString("token", "Error"));
                return header;
            }
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();

                params.put("n_student",numE );
                params.put("ouser", alumno1.getText().toString()+" "+alumno2.getText().toString());
                params.put("latitud",latV);
                params.put("longitud",lonV);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(peticion);

    }
    private void agregarRespuesta(String response) {
        try{
            JSONObject r = new JSONObject(response);
            if(r.getString("add").compareTo("y")==0){
                Toast.makeText(MainActivity3.this, "Alerta enviada!", Toast.LENGTH_SHORT).show();
                alumno1.setText("");
                alumno2.setText("");
                startActivity(new Intent(this, MapsActivity.class));
                //llenar();
            }else{
                Toast.makeText(MainActivity3.this, "Error no se pudo enviar la alerta", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }
}

