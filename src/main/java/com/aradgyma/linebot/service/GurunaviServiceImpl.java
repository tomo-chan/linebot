package com.aradgyma.linebot.service;

import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

@Service
public class GurunaviServiceImpl implements GurunaviService {

    private final String endpointRestSearch = "https://api.gnavi.co.jp/RestSearchAPI/20150630/";

    public ArrayList<Restaurant> getRestaurantList() throws BotException {
        try {
            URL restSearch = new URL(endpointRestSearch);
            HttpURLConnection http = (HttpURLConnection)restSearch.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            //Jackson
            ObjectMapper mapper = new ObjectMapper();

            return parseRestaurant(mapper.readTree(http.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BotException(e);
        }
    }

    private ArrayList<Restaurant> parseRestaurant(@NonNull JsonNode nodeList) {
        ArrayList<Restaurant> restaurantList = new ArrayList<>();

        //トータルヒット件数
        String hitcount   = "total:" + nodeList.path("total_hit_count").asText();
        System.out.println(hitcount);
        //restのみ取得
        JsonNode restList = nodeList.path("rest");
        //店舗番号、店舗名、最寄の路線、最寄の駅、最寄駅から店までの時間、店舗の小業態を出力
        for (JsonNode r : restList) {
            String id = r.path("id").asText();
            String name = r.path("name").asText();
            String line = r.path("access").path("line").asText();
            String station = r.path("access").path("station").asText();
            String walk = r.path("access").path("walk").asText() + "分";
            StringBuilder categories = new StringBuilder();

            for (JsonNode n : r.path("code").path("category_name_s")) {
                categories.append(n.asText());
            }
            Restaurant restaurant = Restaurant.builder()
                    .id(id)
                    .name(name)
                    .line(line)
                    .station(station)
                    .walk(walk)
                    .build();

            restaurantList.add(restaurant);
            System.out.println(id + "¥t" + name + "¥t" + line + "¥t" + station + "¥t" + walk + "¥t" + categories.toString());
        }
        return restaurantList;
    }
}
