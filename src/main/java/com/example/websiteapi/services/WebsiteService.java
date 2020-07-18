package com.example.websiteapi.services;

import com.example.websiteapi.entities.WebsiteWithID;
import com.example.websiteapi.repositories.WebsiteRepository;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.CustomLog;
import lombok.NonNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

/**
 * Service to access websites.
 */
@Service
@CustomLog
public class WebsiteService {
    @Autowired
    private WebsiteRepository websiteRepository;

    /**
     * Given a list of IDs, attempt to fetch the corresponding websites from the database.
     * <p>
     * If an ID of a non-existent website is provided, the ID will just be ignored.
     *
     * @param ids id of the websites to query
     * @return all the websites that match the IDs provided
     */
    public ImmutableList<WebsiteWithID> getWebsitesById(@NonNull ImmutableCollection<UUID> ids) {
        log.debug("get_websites_by_id", ImmutableMap.of(
                "ids", ids.toString()
        ));
        if (ids.isEmpty()) {
            return ImmutableList.of();
        }

        // If only one ID is provided, use the getWebsite method directly.
        // If several IDs are provided, get all the websites and filter only the ones selected.
        // (Filtering all websites is faster than fetching all the URLs one by one).
        // This is a very random behavior, just so I can call two different methods in this service.
        if (ids.size() == 1) {
            val websiteId = ids.iterator().next();
            val website = websiteRepository.get(websiteId);
            return website.map(value -> ImmutableList.of(
                    new WebsiteWithID(
                            value,
                            websiteId
                    )
            )).orElseGet(ImmutableList::of);
        }

        val idSet = new HashSet<>(ids);
        val allWebsites = websiteRepository.list();
        return allWebsites.stream()
                .filter(website -> idSet.contains(website.getId()))
                .collect(ImmutableList.toImmutableList());
    }
}
