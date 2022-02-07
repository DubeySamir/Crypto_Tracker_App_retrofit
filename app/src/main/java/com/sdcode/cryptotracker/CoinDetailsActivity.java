package com.sdcode.cryptotracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdcode.cryptotracker.apimanager.CoinGeckoApi;
import com.sdcode.cryptotracker.apimanager.singlecoinhistoricalinfo.Root;
import com.sdcode.cryptotracker.base.BaseClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinDetailsActivity extends BaseClass {
    ImageView imgLoGo;
    TextView tv_symbol, tv_cp, tv_mcCp, tv_mcRk, tv_description;
    TextView tv_cpLbl,tv_mcCpLbl,tv_mcRkLbl;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        initUi();

        initData();

        Intent intent = getIntent();
        String coinId = intent.getStringExtra("coinId");
        getCoinInfo(coinId);
    }


    private void getCoinInfo(String coinId) {
        Call<Root> coinData = CoinGeckoApi.coinService.getSingleCoinData(coinId);

        coinData.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {

                if (!response.isSuccessful()) {
                    tv_description.setText("Code: " + response.code());
                    return;
                }

                Root coinData = response.body();

                String id = coinData.id;                                                            //bitcoin
                String symbol = coinData.symbol;                                                    //btc
                String name = coinData.name;                                                        //Bitcoin
                String current_price = Double.toString(coinData.market_data.current_price.usd);     //48000.456
                String mcRk = Integer.toString(coinData.market_cap_rank);                           //1
                String mcCp = Long.toString(coinData.market_data.market_cap.usd);                   //2165189451
                String imgURL = coinData.image.large;

                progressDialog.dismiss();

                tv_cpLbl.setText("Current Price");
                tv_mcCpLbl.setText("Market Capital");
                tv_mcRkLbl.setText("Market Rank");
                lineChart.setVisibility(View.VISIBLE);
                Picasso.get().load(imgURL).into(imgLoGo);
                tv_symbol.setText(symbol);
                tv_cp.setText(current_price);
                tv_mcCp.setText(mcCp);
                tv_mcRk.setText(mcRk);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tv_description.setText(Html.fromHtml(coinData.description.en, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tv_description.setText(Html.fromHtml(coinData.description.en));
                }

                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                tv_description.setText(t.getMessage());

            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        ArrayList<Entry> lineList = new ArrayList<>();
        LineDataSet lineDataSet;

        lineList.add(new Entry(10f,100f));
        lineList.add(new Entry(10f,200f));
        lineList.add(new Entry(20f,500f));
        lineList.add(new Entry(30f,400f));
        lineList.add(new Entry(40f,300f));
        lineList.add(new Entry(50f,200f));
        lineList.add(new Entry(60f,600f));
        lineList.add(new Entry(70f,100f));
        lineList.add(new Entry(80f,150f));

        lineDataSet = new LineDataSet(lineList, "Count");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColor(Color.BLACK);

//        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.BLUE);
        lineDataSet.setValueTextSize(13f);
        lineDataSet.setDrawFilled(true);


    }

    @Override
    public void initUi() {
        super.initUi();

        progressDialog = new ProgressDialog(CoinDetailsActivity.this);
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


        imgLoGo = findViewById(R.id.imgLoGo);
        tv_symbol = findViewById(R.id.tv_symbol);
        tv_cp = findViewById(R.id.tv_cp);
        tv_mcCp = findViewById(R.id.tv_mcCp);
        tv_mcRk = findViewById(R.id.tv_mcRk);
        tv_description = findViewById(R.id.tv_description);
        tv_cpLbl = findViewById(R.id.tv_cpLbl);
        tv_mcCpLbl = findViewById(R.id.tv_mcCpLbl);
        tv_mcRkLbl = findViewById(R.id.tv_mcRkLbl);

        lineChart = findViewById(R.id.lineChart);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setVisibility(View.INVISIBLE);
    }
}