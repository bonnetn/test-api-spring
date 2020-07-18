package com.example.websiteapi.controllers;

import com.example.websiteapi.entities.WebsiteWithID;
import com.example.websiteapi.services.WebsiteService;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.CustomLog;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@CustomLog
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
    @ResponseBody
    public ResponseEntity<?> fetch(@RequestParam("ids") List<String> ids) {
        val validAndInvalidUUIDs = mapStringListToUUIDs(ids);
        val validUUIDs = validAndInvalidUUIDs.getValue0();
        val invalidUUIDs = validAndInvalidUUIDs.getValue1();

        if (!invalidUUIDs.isEmpty()) {
            log.warn("get_website_request_invalid_uuid", ImmutableMap.of(
                    "ids", ids.toString(),
                    "invalid_ids", invalidUUIDs.toString()
            ));
            return ResponseEntity.badRequest()
                    .body(new JSONBadUUIDResult(invalidUUIDs));
        }

        val result = websiteService.getWebsitesById(validUUIDs)
                .stream()
                .map(JSONWebsiteResult::from)
                .collect(Collectors.toList());

        log.info("get_website_request_success", ImmutableMap.of(
                "ids", ids.toString(),
                "website_count", Integer.toString(result.size())
        ));
        return ResponseEntity.ok(result);
    }

    /**
     * Attempts to convert a list of strings to a list of UUIDs.
     *
     * @param ids string uuids
     * @return a list of valid uuids, and a list of strings that could not be converted to UUIDs
     */
    private Pair<ImmutableCollection<UUID>, ImmutableCollection<String>> mapStringListToUUIDs(@NonNull List<String> ids) {
        val uuidAndString = ids.stream()
                .map(id -> Pair.with(id, mapStringToUUID(id)))
                .collect(ImmutableList.toImmutableList());

        val invalidUUIDs = uuidAndString.stream()
                .filter(t -> t.getValue1().isEmpty())
                .map(Pair::getValue0)
                .collect(ImmutableList.toImmutableList());

        val validUUIDs = uuidAndString.stream()
                .filter(t -> t.getValue1().isPresent())
                .map(Pair::getValue1)
                .map(Optional::get)
                .collect(ImmutableList.toImmutableList());

        return Pair.with(validUUIDs, invalidUUIDs);
    }

    /**
     * Maps a string to a Java UUID. Does validation and return Optional.empty() in case of failure.
     *
     * @param id string representation of a uuid
     * @return a uuid object
     */
    private Optional<UUID> mapStringToUUID(@NonNull String id) {
        try {
            return Optional.of(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
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

/**
 * JSON serializable class that for returning a bad UUID error.
 */
@Value
@JsonPropertyOrder({"invalid_uuids"})
class JSONBadUUIDResult {
    @NonNull ImmutableCollection<String> invalid_uuids;

    @JsonGetter("reason")
    public String reason() {
        return "bad uuid";
    }
}
