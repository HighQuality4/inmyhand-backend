package com.inmyhand.refrigerator.recipe.domain.enums;

public enum CookingTimeEnum {

    WITHIN_5_MINUTES("5분 이내"),
    FROM_5_TO_10_MINUTES("5분~10분"),
    FROM_10_TO_30_MINUTES("10분~30분"),
    FROM_30_TO_60_MINUTES("30분~1시간"),
    FROM_60_TO_120_MINUTES("1시간~2시간"),
    OVER_120_MINUTES("2시간 이상");

    private final String label;

    CookingTimeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}