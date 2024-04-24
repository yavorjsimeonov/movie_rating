package com.tues.softdev.movierating.service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.Credentials;
import org.json.JSONObject;

public class SpotifySearchService {

  private String clientId = "0e9d85cb11a8482eb9a48b31feebc07e";
  private String clientSecret = "9f9dc322eba740a6b413d6ddfd043eba";
  private String accessToken;
  private OkHttpClient client = new OkHttpClient();

  public SpotifySearchService() {
    authenticate();
  }

  private void authenticate() {
    try {
      String credentials = Credentials.basic(clientId, clientSecret);
      RequestBody body = RequestBody.create("grant_type=client_credentials", MediaType.get("application/x-www-form-urlencoded"));
      Request request = new Request.Builder()
          .url("https://accounts.spotify.com/api/token")
          .header("Authorization", credentials)
          .post(body)
          .build();

      try (Response response = client.newCall(request).execute()) {
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        accessToken = jsonObject.getString("access_token");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String searchSoundtrack(String movieTitle) {
    try {
      String encodedTitle = java.net.URLEncoder.encode(movieTitle + " soundtrack", java.nio.charset.StandardCharsets.UTF_8);
      Request request = new Request.Builder()
          .url("https://api.spotify.com/v1/search?q=" + encodedTitle + "&type=album&limit=1")
          .addHeader("Authorization", "Bearer " + accessToken)
          .build();

      try (Response response = client.newCall(request).execute()) {
        String jsonResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (jsonObject.getJSONObject("albums").getJSONArray("items").length() > 0) {
          return jsonObject.getJSONObject("albums").getJSONArray("items").getJSONObject(0).getJSONObject("external_urls").getString("spotify");
        } else {
          return "No soundtrack found";
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed to fetch soundtrack";
    }
  }
}
