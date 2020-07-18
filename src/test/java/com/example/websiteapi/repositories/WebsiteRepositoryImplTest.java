package com.example.websiteapi.repositories;

import com.example.websiteapi.entities.Website;
import com.example.websiteapi.entities.WebsiteWithID;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class WebsiteRepositoryImplTest {
    final UUID websiteID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Autowired
    private WebsiteRepository websiteRepository;

    @Test
    void get_existingUUID() {
        assertThat(websiteRepository.get(websiteID))
                .isEqualTo(Optional.of(new Website("test url", "test name")));
    }

    @Test
    void get_nonExistingUUID() {
        assertThat(websiteRepository.get(UUID.fromString("11111111-1111-1111-1111-111111111111")))
                .isNotPresent();
    }

    @Test
    void list() {
        assertThat(websiteRepository.list())
                .isEqualTo(ImmutableList.of(
                        new WebsiteWithID(new Website("test url", "test name"), websiteID)
                ));
    }
}