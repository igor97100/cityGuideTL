package gr.teicm.cityguidetl.cityguidetl.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import gr.teicm.cityguidetl.cityguidetl.Adapters.CityListAdapter;
import gr.teicm.cityguidetl.cityguidetl.Entities.City;
import gr.teicm.cityguidetl.cityguidetl.R;
import gr.teicm.cityguidetl.cityguidetl.Services.CityService;
import gr.teicm.cityguidetl.cityguidetl.Utilities.AuthenticatorInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private GoogleMap googleMap;
	private MapView mapView;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static Retrofit retro;
    public static CityService cityService;

    public static String accessToken;
    public static String refreshToken;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.citiesList);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new AuthenticatorInterceptor()).build();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://10.0.2.2:8080").client(client).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        cityService = retrofit.create(CityService.class);
        Call<ArrayList<City>> call = cityService.getCities();

        call.enqueue(new Callback<ArrayList<City>>() {
            @Override
            public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
                try {
                    ArrayList<City> cities = response.body();
                    listView.setAdapter(new CityListAdapter(MainActivity.this, cities));
                }
                catch(Exception e) {
                    Toast.makeText(MainActivity.this, "exception thrown", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ArrayList<City>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

     //   Intent intent = new Intent(this, PointsActivity.class);
     //   startActivity(intent);

    }



}


