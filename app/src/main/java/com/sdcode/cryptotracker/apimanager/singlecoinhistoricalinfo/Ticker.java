package com.sdcode.cryptotracker.apimanager.singlecoinhistoricalinfo;

import java.util.Date;

public class Ticker{
    public String base;
    public String target;
    public Market market;
    public double last;
    public double volume;
    public ConvertedLast converted_last;
    public ConvertedVolume converted_volume;
    public String trust_score;
    public double bid_ask_spread_percentage;
    public Date timestamp;
    public Date last_traded_at;
    public Date last_fetch_at;
    public boolean is_anomaly;
    public boolean is_stale;
    public String trade_url;
    public Object token_info_url;
    public String coin_id;
    public String target_coin_id;
}
