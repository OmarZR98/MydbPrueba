package com.example.mydbprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

   // private RequestQueue queue;
    private ArrayList temperatura,fecha;
    private ListView listview;
    Context ctx;

    String url="192.168.0.10/gez/query.php";//URL del servidor PHP



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   queue= Volley.newRequestQueue(this);
        ctx=getApplicationContext();

        listview=(ListView)findViewById(R.id.listview);
        temperatura=new ArrayList();
        fecha=new ArrayList();
        descargarDatos();
    }

    private void descargarDatos() {
        temperatura.clear();
        fecha.clear();

        final ProgressDialog progressdialog=new ProgressDialog(MainActivity.this);
        progressdialog.setMessage("Cargando datos...");
        progressdialog.show();
        AsyncHttpClient client =new AsyncHttpClient();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    progressdialog.dismiss();
                    try{
                        JSONArray jsonArray=new JSONArray(new String(responseBody));
                        for(int i=0;i<jsonArray.length();i++){

                            temperatura.add(jsonArray.getJSONObject(i).getString("temperatura"));
                            fecha.add(jsonArray.getJSONObject(i).get("fecha"));
                        }
                        Toast.makeText(ctx,temperatura.get(0).toString(), Toast.LENGTH_LONG).show();
                        listview.setAdapter(new CustomAdapter(ctx));

                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


    }
/*
    public void obtenerDatosVolley(){

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray miJSONARRAY =response.getJSONArray("HTML");

                            for(int i=0;i<miJSONARRAY.length();i++){

                                JSONObject miJSONObject=miJSONARRAY.getJSONObject(i);
                                String str=miJSONObject.getString("Y");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        queue.add(request);
    }*/
public class CustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater layoutInflater;
    TextView tv_temperatura,tv_fecha;

    public CustomAdapter(Context applicationContext){
        this.ctx=applicationContext;
        layoutInflater=(LayoutInflater)this.ctx.getSystemService(LAYOUT_INFLATER_SERVICE);///SI O JALA BUSCALE AQUI

    }
    @Override
    public int getCount() {
        return -5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup viewGroup=(ViewGroup) layoutInflater.inflate(R.layout.list_view_item,null);

        tv_temperatura=(TextView) viewGroup.findViewById(R.id.tv_temperatura);
        tv_fecha=(TextView) viewGroup.findViewById(R.id.tv_fecha);

        return viewGroup;
    }
    }

}

