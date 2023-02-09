package com.ruoyi.system.domain;

public class OgReviewChangingDateConfig {
    private Long id;
    private String dayOfWeek;
    private String aheadOfHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getAheadOfHours() {
        return aheadOfHours;
    }

    public void setAheadOfHours(String aheadOfHours) {
        this.aheadOfHours = aheadOfHours;
    }
}

enum DAY_OF_WEEK {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;
}
