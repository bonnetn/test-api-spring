package com.example.websiteapi.entities;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

/**
 * A website and its API handle.
 */
@Value
public class WebsiteWithID {
    @NonNull Website website;
    @NonNull UUID id;
}
