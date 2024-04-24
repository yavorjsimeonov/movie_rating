package com.tues.softdev.movierating.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleImageSearchService {

  private final String apiKey = "AIzaSyAHVJ99kx-HhgK10mFZEPVOAjl-kBJM1aE";
  private final String searchEngineId = "b0788b924b0d64d87";

  public String fetchFirstImageUrl(String query) {
    try {
      String encodedQuery = URLEncoder.encode(query + " movie", StandardCharsets.UTF_8);
      String url = String.format("https://www.googleapis.com/customsearch/v1?q=%s&cx=%s&searchType=image&key=%s&num=1",
          encodedQuery, searchEngineId, apiKey);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .build();
      HttpClient client = HttpClient.newHttpClient();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      JSONObject jsonResponse = new JSONObject(response.body());
      JSONArray items = jsonResponse.getJSONArray("items");
      if (items != null && items.length() > 0) {
        return items.getJSONObject(0).getString("link");
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
}

