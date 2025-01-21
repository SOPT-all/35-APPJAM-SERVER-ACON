package com.acon.server.spot.api.response;

import java.util.List;

public record SearchSuggestionListResponse(
        List<SearchSuggestionResponse> searchSuggestionList
) {

}
