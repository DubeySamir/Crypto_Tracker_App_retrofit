package com.sdcode.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdcode.cryptotracker.adapter.CoinListRVAdapter;
import com.sdcode.cryptotracker.apimanager.CoinGeckoApi;
import com.sdcode.cryptotracker.apimanager.allcoinsinfo.Coins;
import com.sdcode.cryptotracker.base.BaseClass;
import com.sdcode.cryptotracker.models.CoinListModelClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseClass {
    RecyclerView recyclerViewCoinList;
    Button btn_loadCoinList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        recyclerViewCoinList = findViewById(R.id.recyclerViewCoinList);
        btn_loadCoinList = findViewById(R.id.btn_loadCoinList);
        recyclerViewCoinList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getCoinsList();

        btn_loadCoinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoinsList();
            }
        });
    }
    @Override
    public void initUi(){
        super.initUi();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("ProgressDialog"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();

    }

    private void getCoinsList() {
        Log.d("getCoinsList", "getCoinsList: First");
        ArrayList<CoinListModelClass> objectModelClassList = new ArrayList<>();

        Call<List<Coins>> coinList = CoinGeckoApi.getService().getCoinList();

        coinList.enqueue(new Callback<List<Coins>>() {
            @Override
            public void onResponse(Call<List<Coins>> call, Response<List<Coins>> response) {
                Log.d("getCoinsList", "getCoinsList: Second");

                if (!response.isSuccessful()) {
                    Log.d("getCoinsList", "getCoinsList: third");
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("getCoinsList", "getCoinsList: Fourth");
                List<Coins> coinList = response.body();

                Coins[] values = new Coins[coinList.size()];

                for (int i = 0; i < coinList.size(); i++) {
                    values[i] = coinList.get(i);
                    objectModelClassList.add(new CoinListModelClass(values[i].getId(), values[i].getImage(), values[i].getSymbol(), values[i].getName(), values[i].getCurrentPrice()));
                }
                progressDialog.dismiss();
                CoinListRVAdapter adapter = new CoinListRVAdapter(objectModelClassList);
                recyclerViewCoinList.setAdapter(adapter);

                adapter.setOnItemClickListener(new CoinListRVAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String coinId = adapter.getCoinId(position);
                        Toast.makeText(getApplicationContext(), "Coin id = " + coinId, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), CoinDetailsActivity.class);
                        i.putExtra("coinId", coinId);
                        startActivity(i);
                    }
                });

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Coins>> call, Throwable t) {
                Log.d("getCoinsList", "getCoinsList: Fifth");
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("getCoinsList", "getCoinsList: Sixth");
    }
}

