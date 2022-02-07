package com.sdcode.cryptotracker.apimanager;

import com.sdcode.cryptotracker.apimanager.allcoinsinfo.Coins;
import com.sdcode.cryptotracker.apimanager.singlecoinhistoricalinfo.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CoinGeckoApi {

    //    Used Coin gecko free API
    public static final String KEY = "";
    private static final String currency = "USD";

    //    Gives 100 currencies info, called on main page
//    https://api.coingecko.com/api/v3/coins/markets?vs_currency=USD&order=market_cap_desc&per_page=10&page=1&sparkline=false/
//    public static final String CoinListURL = "https://api.coingecko.com/api/v3/coinList/markets?vs_currency=" + currency + "&order=market_cap_desc&per_page=10&page=1&sparkline=false/";
    public static final String CoinListURL = "https://api.coingecko.com/api/v3/coins/";

/*
    //    Gives Single Coin info
    public static final String SingleCoinURL = "https://api.coingecko.com/api/v3/coinList/${id}";

    //    Gives Single Coin Historical info
    public static final String HistoricalChartURL = "https://api.coingecko.com/api/v3/coinList/${id}/market_chart?vs_currency=${currency}&days=${days}";

    //    Gives next 10 items in main page
    public static final String TrendingCoinsURL = "https://api.coingecko.com/api/v3/coinList/markets?vs_currency=${currency}&order=gecko_desc&per_page=10&page=1&sparkline=false&price_change_percentage=24h";
*/
    public static CoinService coinService = null;

    public static CoinService getService(){
        if (coinService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CoinListURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            coinService = retrofit.create(CoinService.class);
        }
        return coinService;
    }

    public interface CoinService {

        @GET("markets/?vs_currency=USD&order=market_cap_desc&per_page=100&page=1&sparkline=false")
        Call<List<Coins>> getCoinList();

        @GET("{coinId}")
        Call<Root> getSingleCoinData(@Path("coinId")  String id);
    }
}
/*
* GET - used for requirements
* POST - passed data in body and used for insert
* PUT - used for update
* DELETE - used for delete
* task - Learn all methods how they works
* */