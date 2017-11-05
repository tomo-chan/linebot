package com.aradgyma.linebot.service;

import com.aradgyma.linebot.GurunaviProperties;
import com.aradgyma.linebot.exception.BotException;
import com.aradgyma.linebot.model.gurunavi.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


@Service
public class GurunaviServiceImpl implements GurunaviService {

    private final GurunaviProperties gurunaviProperties;

    private final String endpointRestSearch = "https://api.gnavi.co.jp/RestSearchAPI/20150630/";

    // 範囲
    private String range = "1";
    // 返却形式
    private String format = "json";

    @Autowired
    public GurunaviServiceImpl(GurunaviProperties gurunaviProperties) {
        this.gurunaviProperties = gurunaviProperties;
    }

    public ArrayList<Restaurant> getRestaurantList(@NotNull String message, @NonNull Double lat, @NotNull Double lon) throws BotException {
        try {
            String prmFormat = "?format=" + format;
            String prmKeyid = "&keyid=" + gurunaviProperties.getAccesskey();
            String prmLat = "&latitude=" + lat;
            String prmLon = "&longitude=" + lon;
            String prmRange = "&range=" + range;
            String prmFreeword = "&freeword=" + URLEncoder.encode(message, "UTF-8");

            // URI組み立て
            String uri = endpointRestSearch;
            uri += prmFormat;
            uri += prmKeyid;
            uri += prmLat;
            uri += prmLon;
            uri += prmRange;
            uri += prmFreeword;

            URL restSearch = new URL(uri);
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

//        System.out.println(nodeList.toString());

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
            int walk = r.path("access").path("walk").asInt();
            String imageUrl = r.path("image_url").path("shop_image1").asText();
            String url = r.path("url").asText();
            Double latitude = r.path("latitude").asDouble();
            Double longitude = r.path("longitude").asDouble();
            String address = r.path("address").asText();
            String tel = r.path("tel").asText();
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
                    .imageUrl(imageUrl)
                    .siteUrl(url)
                    .lat(latitude)
                    .lon(longitude)
                    .address(address)
                    .tel(tel)
                    .build();

            restaurantList.add(restaurant);
        }
        return restaurantList;
    }
}
