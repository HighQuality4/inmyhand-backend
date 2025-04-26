package com.inmyhand.refrigerator.common.redis;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@Component
public class RedisKeyManager {

    // 도메인별 프리픽스 정의
    private static final String AUTH_PREFIX = "AUTH";     // 인증/로그인 관련
    private static final String SEARCH_PREFIX = "SEARCH"; // 검색 관련
    private static final String TEMP_PREFIX = "TEMP";     // 임시저장 관련
    private static final String BLACK_LIST = "BLACK_LIST"; // 블랙리스트 관련
    private static final String EMAIL_AUTH = "EMAIL_AUTH"; // 이메일 인증 관련
    private static final String EMAIL_VERIFIED = "EMAIL_VERIFIED";


    // 로그인 관련 키 생성
    public String getLoginKey(Long userId) {
        return String.format("%s:REFRESH_TOKEN:%s", AUTH_PREFIX, userId);
    }

    // 인기 검색어 관련 키 생성 - 레시피 검색 전용 키로 수정
    public String getDailyPopularRecipeKey() {
        LocalDate today = LocalDate.now();
        return String.format("%s:POPULAR:RECIPE:DAILY:%s", SEARCH_PREFIX, today.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    public String getWeeklyPopularRecipeKey() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return String.format("%s:POPULAR:RECIPE:WEEKLY:%s", SEARCH_PREFIX, startOfWeek.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    public String getMonthlyPopularRecipeKey() {
        LocalDate today = LocalDate.now();
        return String.format("%s:POPULAR:RECIPE:MONTHLY:%s", SEARCH_PREFIX, today.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }

    // 블랙리스트 키 생성
    public String getBlackListKey(Long userId) {
        return String.format("%s:%s", BLACK_LIST, userId);
    }

    // 이메일 인증 관련 키 생성
    public String getEmailAuthKey(int code) {
        return String.format("%s:%s", EMAIL_AUTH, code);
    }

    // 이메일 인증 완료 키 생성
    public String getEmailVerifiedKey(String email) {
        return String.format("%s:%s", EMAIL_VERIFIED, email);
    }

    // 임시저장 관련 키 생성
    public String getTempSaveKey(String userId, String contentType) {
        return String.format("%s:SAVE:%s:%s", TEMP_PREFIX, contentType, userId);
    }
}
