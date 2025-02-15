package com.acon.server.member.api.response;

public record PreSignedUrlResponse(
        String fileName,
        String preSignedUrl
) {

    public static PreSignedUrlResponse of(String fileName, String preSignedUrl) {
        return new PreSignedUrlResponse(fileName, preSignedUrl);
    }
}
