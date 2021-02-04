package com.example.applicationjsoncovid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView txtHello, txtCases, txtNewCases, txtDead,txtHealed,txtChange;
    EditText txtDate, txtProv;
    Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtHello=findViewById(R.id.txtHello);
        txtCases=findViewById(R.id.txtCases);
        txtNewCases=findViewById(R.id.txtNewCases);
        txtDead=findViewById(R.id.txtDead);
        txtHealed=findViewById(R.id.txtHealed);
        txtChange=findViewById(R.id.txtChange);
        txtDate=findViewById(R.id.txtDate2);
        txtProv=findViewById(R.id.txtProv);
        btnSearch=findViewById(R.id.btnSearch);
        getVersion();
        String vers[] = txtHello.getText().toString().split(" ");
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

    public void afficher (View view) {
        String prov=txtProv.getText().toString();
        String date=txtDate.getText().toString();
        String dates[] = date.split(" ");
        if (dates[0].length()==1) {
            dates[0]="0"+dates[0];
        }
        String date2="";
        Log.d("month",dates[1]);
        switch (dates[1]) {
            case "Janvier": date2="01"; break;
            case "Fevrier": date2="02"; break;
            case "Mars": date2="03"; break;
            case "Avril": date2="04"; break;
            case "Mai": date2="05"; break;
            case "Juin": date2="06"; break;
            case "Juillet": date2="07"; break;
            case "Aout": date2="08"; break;
            case "Septembre": date2="09"; break;
            case "Octobre": date2="10"; break;
            case "Novembre": date2="11"; break;
            case "Decembre": date2="12"; break;
        }
        int intDate = Integer.valueOf(dates[2]+date2+dates[0]);
        date2=dates[2]+"-"+date2+"-"+dates[0];
        String vers[] = txtHello.getText().toString().split(" ");
        int intVers = Integer.valueOf(vers[1].replaceAll("-",""));
        if (intDate>intVers||intDate<20200125) {
            Toast.makeText(MainActivity.this, "Entrez un date entre 2020-01-25 et "+vers[1], Toast.LENGTH_LONG).show();
        } else {
            String real = "ERROR";
            switch (prov) {
                case "Ontario":
                    real = "ON";
                    break;
                case "Alberta":
                    real = "AB";
                    break;
                case "Colombie britannique":
                    real = "BC";
                    break;
                case "Manitoba":
                    real = "MB";
                    break;
                case "Nouveau-Brunswick":
                    real = "NB";
                    break;
                case "Terre-Neuve-et-Labrador":
                    real = "NL";
                    break;
                case "Northwest Territories":
                    real = "NWT";
                    break;
                case "Nouvelle-Ecosse":
                    real = "Ns";
                    break;
                case "Nunavut":
                    real = "NU";
                    break;
                case "Ile-du-Prince-Edouard":
                    real = "PE";
                    break;
                case "Quebec":
                    real = "QB";
                    break;
                case "Saskatchewan":
                    real = "SK";
                    break;
                case "Yukon":
                    real = "YT";
                    break;
            }
            if (real.equals("ERROR")) {
                Toast.makeText(MainActivity.this, "Province inconnue, utilise aucune accent. Exemples: Ontario ou Nouvelle-Ecosse", Toast.LENGTH_LONG).show();
            } else {
                Log.d("affiching", date2 + " and " + real);
                String url = "https://api.opencovid.ca/summary?loc=" + real + "&date=" + date2;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("summary");
                            JSONObject object = jsonArray.getJSONObject(0);
                            int cases = object.getInt("active_cases");
                            int newCases = object.getInt("cases");
                            int dead = (int) object.getDouble("deaths");
                            int recovered = (int) object.getDouble("recovered");
                            int change = object.getInt("active_cases_change");

                            txtDate.setText("");
                            txtProv.setText("");

                            txtCases.setText("   " + Integer.toString(cases) + "   ");
                            txtNewCases.setText("   " + Integer.toString(newCases) + "   ");
                            txtHealed.setText("   " + Integer.toString(recovered) + "   ");
                            txtDead.setText("   " + Integer.toString(dead) + "   ");
                            txtChange.setText("   " + Integer.toString(change) + "   ");

                            InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (getCurrentFocus() != null) {
                                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("IT CAUGHT", "ERROR IT CUAGHT");
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERRORRESPONS", "EROORO RERADEIOFORG HI" + error);
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(jsonObjectRequest);
            }
        }
    }
}