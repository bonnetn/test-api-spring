package com.example.websiteapi.controllers;

import com.example.websiteapi.entities.Website;
import com.example.websiteapi.entities.WebsiteWithID;
import com.example.websiteapi.services.WebsiteService;
import com.google.common.collect.ImmutableList;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebsiteRestControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Autowired
    WebsiteRestController websiteRestController;
    @MockBean
    private WebsiteService websiteService;

    @Test
    public void shouldFetchProfile() throws Exception {
        UUID uuid0 = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID uuid1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

        Mockito.when(websiteService.getWebsitesById(ImmutableList.of(uuid0, uuid1)))
                .thenReturn(ImmutableList.of(
                        new WebsiteWithID(
                                new Website("url", "name"),
                                uuid0
                        ),
                        new WebsiteWithID(
                                new Website("url2", "name2"),
                                uuid1
                        )
                ));

        List<String> given = Arrays.asList(uuid0.toString(), uuid1.toString());
        assertThat(websiteRestController.fetch(given))
                .isEqualTo(Arrays.asList(
                        new JSONWebsiteResult("name", "url", uuid0.toString()),
                        new JSONWebsiteResult("name2", "url2", uuid1.toString())
                ));
    }

    @Test
    public void shouldReturnBadRequestOnBadUUID() {
        List<String> given = Arrays.asList("bad uuid", "other bad uuid");

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            websiteRestController.fetch(given);
        });
        assertThat(exception.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

}