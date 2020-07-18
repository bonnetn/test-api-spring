package com.example.websiteapi.controllers;

import com.example.websiteapi.entities.WebsiteWithID;
import com.example.websiteapi.services.WebsiteService;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class WebsiteRestController {
    @Autowired
    private WebsiteService websiteService;

    /**
     * Fetches all the websites from the database.
     *
     * @param ids a list of websites IDs to return
     * @return websites
     */
    @GetMapping("/website")
    public List<JSONWebsiteResult> fetch(@RequestParam("ids") List<String> ids) {
        val uuids = ids.stream()
                .map(this::mapStringToUUID)
                .collect(ImmutableList.toImmutableList());

        val websites = websiteService.getWebsitesById(uuids);

        return websites.stream()
                .map(JSONWebsiteResult::from)
                .collect(Collectors.toList());
    }

    /**
     * Maps a string to a Java UUID. Does validation and return Spring exception in case of failure.
     * <p>
     * If the id is not a proper UUID, will return a Spring exception that will raise to a HTTP 400 error.
     *
     * @param id string representation of a uuid
     * @return a uuid object
     */
    private UUID mapStringToUUID(@NonNull String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("invalid UUID: \"%s\"", id), e);
        }
    }

}

/**
 * JSON serializable class that represents a Website.
 */
@Value
@JsonPropertyOrder({"name", "url", "id"})
class JSONWebsiteResult {
    @NonNull String name;
    @NonNull String url;
    @NonNull String id;

    static JSONWebsiteResult from(@NonNull WebsiteWithID website) {
        return new JSONWebsiteResult(
                website.getWebsite().getName(),
                website.getWebsite().getUrl(),
                website.getId().toString()
        );
    }
}
