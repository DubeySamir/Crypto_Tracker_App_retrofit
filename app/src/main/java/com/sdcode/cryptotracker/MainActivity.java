package com.sdcode.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdcode.cryptotracker.apimanager.CoinGeckoApi;
import com.sdcode.cryptotracker.apimanager.allcoinsinfo.Coins;
import com.sdcode.cryptotracker.apimanager.singlecoinhistoricalinfo.Root;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView tv_Coin;
    ListView lv_Coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_Coins = findViewById(R.id.lv_Coins);
        tv_Coin = findViewById(R.id.tv_Coin);

        getCoinsList();
        getCoinInfo("bitcoin");
    }

    private void getCoinInfo(String coinId) {
        Call<Root> coinData = CoinGeckoApi.coinService.getSingleCoinData(coinId);

        coinData.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {

                if (!response.isSuccessful()) {
                    tv_Coin.setText("Code: " + response.code());
                    return;
                }

                Root coinData = response.body();

                tv_Coin.setText("");
                String content = "";
                content += "Id              : " + coinData.id + "\n";
                content += "Symbol          : " + coinData.symbol + "\n";
                content += "Name            : " + coinData.name + "\n";
                content += "Current Price   : " + coinData.market_data.current_price.usd + "\n";
                content += "Market Cap Rank : " + coinData.market_cap_rank + "\n";
                content += "Market Cap      : " + coinData.market_data.market_cap.usd + "\n\n";
                content += "Description     : " + coinData.description.en + "\n";

                tv_Coin.setText(content);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_Coin.append(Html.fromHtml(coinData.description.en, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_Coin.append(Html.fromHtml(coinData.description.en));
                }

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                tv_Coin.setText(t.getMessage());

            }
        });
    }

    private void getCoinsList() {
        Call<List<Coins>> coinList = CoinGeckoApi.getService().getCoinList();

        coinList.enqueue(new Callback<List<Coins>>() {
            @Override
            public void onResponse(Call<List<Coins>> call, Response<List<Coins>> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Coins> coinList = response.body();

                Coins[] values = new Coins[coinList.size()];
                String[] coinDataList = new String[values.length];

                for (int i = 0; i < coinList.size(); i++) {
                    values[i] = coinList.get(i);

                    String content = "";
                    content += "Id: " + values[i].getId() + "\n";
                    content += "Symbol: " + values[i].getSymbol() + "\n";
                    content += "Name: " + values[i].getName() + "\n";
                    content += "Current Price: " + values[i].getCurrentPrice() + "\n\n";

                    coinDataList[i] = content;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, coinDataList);

                lv_Coins.setAdapter(adapter);
                lv_Coins.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String coinId = values[position].getId();
                        getCoinInfo(coinId);
                    }
                });

//
//                for (Coins coin : coinList) {
//                    String content = "";
//                    content += "Id: " + coin.getId() + "\n";
//                    content += "Symbol: " + coin.getSymbol() + "\n";
//                    content += "Name: " + coin.getName() + "\n";
//                    content += "Current Price: " + coin.getCurrentPrice() + "\n\n";
//
//                    tv_Coins.append(content);
//                }

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Coins>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}