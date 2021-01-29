package com.example.applicationjsoncovid;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView txtHello;
    String vers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtHello=findViewById(R.id.txtHello);
        getVersion();
    }

    public void getVersion() {
        String url = "https://api.opencovid.ca/version";
        final String[] ret = {""};
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String version=response.getString("version");
                    Log.d("Fetched: ", "This "+version);
                    String[] ver = version.split(" ");
                    Log.d("Proccessing: ", "This "+ver[0]);
                    txtHello.setText("Version: "+ver[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("IT CAUGHT", "ERROR IT CUAGHT");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORRESPONS","EROORO RERADEIOFORG HI"+error);
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void afficher (String date) {

        String url ="";
    }
}