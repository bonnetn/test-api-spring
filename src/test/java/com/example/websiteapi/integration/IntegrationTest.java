package com.example.websiteapi.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnValidJSONWithWebsite() throws JSONException {
        String id = "00000000-0000-0000-0000-000000000000";
        String url = String.format("http://localhost:%d/website?ids=%s", port, id);
        String resultString = this.restTemplate.getForObject(url, String.class);
        JSONArray resultJson = new JSONArray(resultString);

        assertThat(resultJson.length()).isEqualTo(1);

        JSONObject element = resultJson.getJSONObject(0);
        assertThat(element.getString("id"))
                .isEqualTo(id);
        assertThat(element.getString("name"))
                .isEqualTo("test name");
        assertThat(element.getString("url"))
                .isEqualTo("test url");
    }

    @Test
    public void shouldReturnValidJSONWithoutWebsite() throws JSONException {
        String id = "11111111-1111-1111-1111-111111111111";
        String url = String.format("http://localhost:%d/website?ids=%s", port, id);
        String resultString = this.restTemplate.getForObject(url, String.class);
        JSONArray resultJson = new JSONArray(resultString);

        assertThat(resultJson.length()).isEqualTo(0);
    }

    @Test
    public void shouldReturnBadRequest() throws JSONException {
        String id = "bad uuid";
        String url = String.format("http://localhost:%d/website?ids=%s", port, id);
        ResponseEntity<String> entity = this.restTemplate.getForEntity(url, String.class);

        assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
