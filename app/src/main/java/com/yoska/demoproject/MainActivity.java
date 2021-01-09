package com.yoska.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    StringRequest stringRequest;

    private String TAG = MainActivity.class.getSimpleName();
    private ListView listView;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lst_data);

        new fetchData().execute();
    }

    private class fetchData extends AsyncTask {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading..!!");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String jsonString = null;
            String line = "";


            StringBuffer response = new StringBuffer();

            try {
                URL url = new URL("https://reqres.in/api/users?page=1");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(con.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader((in)));

                while((line = rd.readLine())!= null){
                    response.append(line);
                }

                in.close();
                rd.close();

                jsonString = new String(response);

                if(jsonString != null){
                    JSONObject jsonObj = new JSONObject(jsonString);

                    JSONArray contacts = jsonObj.getJSONArray("data");

                   for(int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String first_name = c.getString("first_name");
                        String email = c.getString("email");
                        String last_name = c.getString("last_name");
                       String avatar = c.getString("avatar");
                        char start = first_name.charAt(0);

                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("first_name", first_name);
                        contact.put("last_name",last_name);
                        contact.put("email", email);
                       contact.put("avatar",avatar);
                        contact.put("start",Character.toString(start));

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                }else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
           ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                    R.layout.item_list, new String[]{ "first_name","email","start"},
                    new int[]{R.id.first_name, R.id.email, R.id.start});

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(MainActivity.this,DetailView.class);
                    intent.putExtra("first_name",contactList.get(position).get("first_name"));
                    intent.putExtra("last_name",contactList.get(position).get("last_name"));
                    intent.putExtra("email",contactList.get(position).get("email"));
                    intent.putExtra("avatar",contactList.get(position).get("avatar"));
                    startActivity(intent);
                }
            });
        }
    }

}