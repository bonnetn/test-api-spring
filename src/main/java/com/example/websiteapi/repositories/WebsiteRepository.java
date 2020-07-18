package com.example.websiteapi.repositories;

import com.example.websiteapi.entities.Website;
import com.example.websiteapi.entities.WebsiteWithID;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Allow retrieval of stored websites.
 */
public interface WebsiteRepository {
    Optional<Website> get(@NonNull UUID id);
    ImmutableList<WebsiteWithID> list();
}


