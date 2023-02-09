package com.ruoyi.common.annotation;

import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DateFormat注解集
 *
 * @author ruoyi
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {

    /**
     * The style pattern to use to format the field.
     * <p>Defaults to 'SS' for short date time. Set this attribute when you wish to format
     * your field in accordance with a common style other than the default style.
     */
    String style() default "SS";

    /**
     * The ISO pattern to use to format the field.
     * <p>The possible ISO patterns are defined in the {@link DateTimeFormat.ISO} enum.
     * <p>Defaults to {@link DateTimeFormat.ISO#NONE}, indicating this attribute should be ignored.
     * Set this attribute when you wish to format your field in accordance with an ISO format.
     */
    DateTimeFormat.ISO iso() default DateTimeFormat.ISO.NONE;

    /**
     * The custom pattern to use to format the field.
     * <p>Defaults to empty String, indicating no custom pattern String has been specified.
     * Set this attribute when you wish to format your field in accordance with a custom
     * date time pattern not represented by a style or ISO format.
     * <p>Note: This pattern follows the original {@link java.text.SimpleDateFormat} style,
     * as also supported by Joda-Time, with strict parsing semantics towards overflows
     * (e.g. rejecting a Feb 29 value for a non-leap-year). As a consequence, 'yy'
     * characters indicate a year in the traditional style, not a "year-of-era" as in the
     * {@link java.time.format.DateTimeFormatter} specification (i.e. 'yy' turns into 'uu'
     * when going through that {@code DateTimeFormatter} with strict resolution mode).
     */
    String pattern() default "";


    /**
     * Common ISO date time format patterns.
     */
    enum ISO {

        /**
         * The most common ISO Date Format {@code yyyy-MM-dd},
         * e.g. "2000-10-31".
         */
        DATE,

        /**
         * The most common ISO Time Format {@code HH:mm:ss.SSSXXX},
         * e.g. "01:30:00.000-05:00".
         */
        TIME,

        /**
         * The most common ISO DateTime Format {@code yyyy-MM-dd'T'HH:mm:ss.SSSXXX},
         * e.g. "2000-10-31T01:30:00.000-05:00".
         * <p>This is the default if no annotation value is specified.
         */
        DATE_TIME,
        String
        ,

        /**
         * Indicates that no ISO-based format pattern should be applied.
         */
        NONE
    }
}