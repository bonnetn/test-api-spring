package com.example.websiteapi.repositories;

import com.example.websiteapi.entities.Website;
import com.example.websiteapi.entities.WebsiteWithID;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.CustomLog;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * In memory database of websites. Only the nil UUID is bound to a website.
 */
@Repository
@CustomLog
public class WebsiteRepositoryImpl implements WebsiteRepository {
    final UUID websiteId = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * Gets a single website.
     *
     * @param id website ID to query
     * @return the website or empty if not found
     */
    @Override
    public Optional<Website> get(@NonNull UUID id) {
        log.debug("get", ImmutableMap.of(
                "uuid", id.toString()
        ));
        if (websiteId.equals(id)) {
            return Optional.of(new Website("test url", "test name"));
        }
        return Optional.empty();
    }

    /**
     * Fetches all the websites stored in database.
     *
     * @return all the websites
     */
    @Override
    public ImmutableList<WebsiteWithID> list() {
        log.debug("list", ImmutableMap.of());
        return ImmutableList.of(
                new WebsiteWithID(new Website("test url", "test name"), websiteId)
        );
    }
}
