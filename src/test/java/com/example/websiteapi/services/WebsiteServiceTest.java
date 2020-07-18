package com.example.websiteapi.services;

import com.example.websiteapi.entities.Website;
import com.example.websiteapi.entities.WebsiteWithID;
import com.example.websiteapi.repositories.WebsiteRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class WebsiteServiceTest {
    final UUID websiteID1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    final UUID websiteID2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    final UUID websiteIDNonExisting = UUID.fromString("33333333-3333-3333-3333-333333333333");

    final WebsiteWithID website1 = new WebsiteWithID(
            new Website(
                    "url_website1",
                    "website1"
            ),
            websiteID1
    );
    final WebsiteWithID website2 = new WebsiteWithID(
            new Website(
                    "url_website3",
                    "website3"
            ),
            websiteID2
    );
    @Autowired
    private WebsiteService websiteService;
    @MockBean
    private WebsiteRepository websiteRepository;

    @BeforeEach
    public void setUp() {
        Mockito.when(websiteRepository.get(websiteID1))
                .thenReturn(Optional.of(website1.getWebsite()));

        Mockito.when(websiteRepository.get(websiteID2))
                .thenReturn(Optional.of(website2.getWebsite()));

        Mockito.when(websiteRepository.get(websiteIDNonExisting))
                .thenReturn(Optional.empty());

        Mockito.when(websiteRepository.list())
                .thenReturn(ImmutableList.of(website1, website2));

    }

    @Test
    void getWebsitesById_noIdProvided() {
        assertThat(websiteService.getWebsitesById(ImmutableList.of()))
                .isEmpty();
    }

    @Test
    void getWebsitesById_OneSingleIDNonExisting() {
        assertThat(websiteService.getWebsitesById(ImmutableList.of(websiteIDNonExisting)))
                .isEqualTo(ImmutableList.of());
    }

    @Test
    void getWebsitesById_OneSingleIDExisting() {
        assertThat(websiteService.getWebsitesById(ImmutableList.of(websiteID1)))
                .isEqualTo(ImmutableList.of(website1));
    }

    @Test
    void getWebsitesById_SeveralIDs() {
        assertThat(websiteService.getWebsitesById(ImmutableList.of(websiteIDNonExisting, websiteID2)))
                .isEqualTo(ImmutableList.of(website2));
    }

}