package com.acon.server.spot.api.response;

public record SearchSuggestionResponse(
        Long spotId,
        String spotName
) {

    public static SearchSuggestionResponse of(Long spotId, String spotName) {
        return new SearchSuggestionResponse(spotId, spotName);
    }
}
