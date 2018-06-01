package com.justapp.googleplacesapi.Location;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.justapp.googleplacesapi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationSearch extends AppCompatActivity {

    private final String API_KEY = "YOUR_API_KEY";
    public static final String TAG = "SHAGUN";

    private String StringToSearch;
    private EditText locationET;
    private ListView locationLV;
    private List<String> all_predictions;
    private HashMap<String, String> hashMap;
    private int selected_button_position = -1;
    private LocationAdapter locationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        locationET = (EditText) findViewById(R.id.editText);
        locationLV = (ListView) findViewById(R.id.listView);
        Button button = (Button) findViewById(R.id.button);

        get_API_response(StringToSearch);

        locationET.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        StringToSearch = locationET.getText().toString();
                        get_API_response(StringToSearch);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );

        locationLV.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.i(TAG, "List View Clicked at: " + position);
                        selected_button_position = position;
                        set_adapter_value(all_predictions, hashMap, selected_button_position);
                    }
                }
        );

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected_button_position != -1)
                            Toast.makeText(LocationSearch.this, all_predictions.get(selected_button_position), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void set_adapter_value(List<String> all_predictions, HashMap<String, String> hashMap, int selected_button_position) {
        locationAdapter = new LocationAdapter(LocationSearch.this, all_predictions, hashMap, selected_button_position);
        locationLV.setAdapter(locationAdapter);
    }

    private void get_API_response(String stringToSearch) {
        all_predictions = new ArrayList<>();
        hashMap = new HashMap<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIs api = retrofit.create(APIs.class);

        Call<ResponseBody> call = api.getPlaces(API_KEY, "(regions)", StringToSearch, "country:ind");

        call.enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String res = null;
                        try {
                            if (response.body() != null) {
                                res = response.body().string();

                                JSONObject jsonObject = new JSONObject(res);
                                String status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("OK")) {
                                    JSONArray predictions = jsonObject.getJSONArray("predictions");
                                    for (int i = 0; i < predictions.length(); i++) {
                                        JSONObject jsonObject1 = predictions.getJSONObject(i);
                                        JSONObject structured_format = jsonObject1.getJSONObject("structured_formatting");
                                        String main_text = structured_format.getString("main_text");
                                        String secondary_text = "";
                                        if (structured_format.has("secondary_text"))
                                            secondary_text = structured_format.getString("secondary_text");

                                        all_predictions.add(main_text);
                                        hashMap.put(main_text, secondary_text);

//                                        Log.i(TAG, "" + all_predictions.size());
//                                        Log.i(TAG, main_text);
                                    }
                                }
                                selected_button_position = -1;
                                set_adapter_value(all_predictions, hashMap, selected_button_position);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(LocationSearch.this, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
