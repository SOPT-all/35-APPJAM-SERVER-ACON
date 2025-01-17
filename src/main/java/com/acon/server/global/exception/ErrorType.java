package com.acon.server.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    /* Common Error */
    /* 400 Bad Request */
    INVALID_PATH_ERROR(HttpStatus.BAD_REQUEST, 40001, "요청 경로의 변수 값이 허용된 형식과 다릅니다."),
    INVALID_FIELD_ERROR(HttpStatus.BAD_REQUEST, 40002, "요청 본문의 필드 값이 허용된 형식과 다릅니다."),
    NO_REQUEST_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, 40003, "요청에 필요한 파라미터가 존재하지 않습니다."),
    NO_REQUEST_HEADER_ERROR(HttpStatus.BAD_REQUEST, 40004, "요청에 필요한 헤더가 존재하지 않습니다."),
    TYPE_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, 40005, "유효하지 않은 값이 입력되었습니다."),
    INVALID_REQUEST_BODY_ERROR(HttpStatus.BAD_REQUEST, 40006, "유효하지 않은 Request Body입니다. 요청 형식 또는 필드를 확인하세요."),
    DATA_INTEGRITY_VIOLATION_ERROR(HttpStatus.BAD_REQUEST, 40007, "데이터 무결성 제약 조건을 위반했습니다."),
    INVALID_ACCESS_TOKEN_ERROR(HttpStatus.BAD_REQUEST, 40008, "유효하지 않은 accessToken입니다."),

    /* 401 Unauthorized */
    EXPIRED_ACCESS_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, 40101, "만료된 accessToken입니다."),

    /* 404 Not Found */
    NOT_FOUND_PATH_ERROR(HttpStatus.NOT_FOUND, 40401, "존재하지 않는 경로입니다."),
    NOT_FOUND_MEMBER_ERROR(HttpStatus.NOT_FOUND, 40402, "존재하지 않는 회원입니다."),

    /* 500 Internal Server Error */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "예상치 못한 서버 에러가 발생했습니다."),

    /* Member Error */
    /* 400 Bad Request */
    INVALID_SOCIAL_TYPE_ERROR(HttpStatus.BAD_REQUEST, 40009, "유효하지 않은 socialType입니다."),
    INVALID_ID_TOKEN_ERROR(HttpStatus.BAD_REQUEST, 40010, "ID 토큰의 서명이 올바르지 않습니다."),
    INVALID_DISLIKE_FOOD_ERROR(HttpStatus.BAD_REQUEST, 40013, "유효하지 않은 dislikeFood입니다."),
    INVALID_CUISINE_ERROR(HttpStatus.BAD_REQUEST, 40014, "유효하지 않은 cuisine입니다."),
    INVALID_SPOT_TYPE_ERROR(HttpStatus.BAD_REQUEST, 40015, "유효하지 않은 spotType입니다."),
    INVALID_SPOT_STYLE_ERROR(HttpStatus.BAD_REQUEST, 40016, "유효하지 않은 spotStyle입니다."),
    INVALID_FAVORITE_SPOT_ERROR(HttpStatus.BAD_REQUEST, 40017, "유효하지 않은 favoriteSpot입니다."),
    INVALID_FAVORITE_SPOT_RANK_SIZE_ERROR(HttpStatus.BAD_REQUEST, 40030, "favoriteSpotRank의 사이즈가 잘못되었습니다."),
    INVALID_FAVORITE_CUISINE_RANK_SIZE_ERROR(HttpStatus.BAD_REQUEST, 40031, "favoriteCuisineRank의 사이즈가 잘못되었습니다."),

    /* 500 Internal Server Error */
    FAILED_DOWNLOAD_GOOGLE_PUBLIC_KEY_ERROR(HttpStatus.BAD_REQUEST, 50002, "구글 공개키 다운로드에 실패하였습니다."),

    /* Spot Error */
    /* 400 Bad Request */
    INVALID_DAY_ERROR(HttpStatus.BAD_REQUEST, 40099, "유효하지 않은 day입니다."),

    /* 404 Not Found */
    NOT_FOUND_SPOT_ERROR(HttpStatus.NOT_FOUND, 40402, "유효한 장소가 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
