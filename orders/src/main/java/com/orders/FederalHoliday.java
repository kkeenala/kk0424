package com.orders;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.DayOfWeek.*;
import static java.time.Month.*;
import static java.util.Objects.requireNonNull;


public enum FederalHoliday {

    INDEPENDENCE_DAY("Independence Day", year -> LocalDate.of(year, JULY, 4)),

    LABOR_DAY("Labor Day", year -> nthDayOfWeek(1, MONDAY, SEPTEMBER, year));

    private static final MonthDay INAUGURATION_DATE = MonthDay.of(JANUARY, 20);

    private final String legalName;
    private final Function<Integer, LocalDate> onDate;

    FederalHoliday(String legalName, Function<Integer, LocalDate> onDate) {
        this.legalName = legalName;
        this.onDate = onDate;
    }

    /**
     * Returns the official, legal name of the Holiday as designated by the Federal government.
     *
     * @return the Holiday's legal name
     */
    public String getLegalName() {
        return legalName;
    }

    /**
     * Returns the observed date of this Holiday for the given year. Fixed date holidays are adjusted so that they fall
     * on a working date. If the fixed date falls on Saturday, it is moved to the preceding Friday; if Sunday, it is
     * moved to the following Monday.
     * <p>
     * Note: if New Year's Day falls on a Saturday on the year provided, the year of returned date will be December 31st
     * of the previous year. This most recently happened in 2011.
     *
     * @param year the calendar year
     * @return the date of this holiday observance for that year
     */
    public LocalDate forYear(int year) {
        return adjustForWeekends(onDate.apply(year));
    }

    /**
     * Returns the date that falls on the nth day of the week for the given year.
     */
    private static LocalDate nthDayOfWeek(int n, DayOfWeek dayOfWeek, Month month, int year) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.dayOfWeekInMonth(n, dayOfWeek));
    }

    /**
     * Adjusts the date, if necessary, so that it does not fall on a weekend. If the date is on a Saturday, returns
     * the previous Friday; if it's on a Sunday, returns the following Monday.
     */
    private static LocalDate adjustForWeekends(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case SATURDAY:
                return date.minusDays(1);
            case SUNDAY:
                return date.plusDays(1);
            default:
                return date;
        }
    }

    /**
     * For the purposes of this class it is assumed 1965 itself is an inauguration year.
     */
    private static boolean isInaugurationYear(int year) {
        return (year - 1965) % 4 == 0;
    }

    public static Optional<LocalDate> inaugurationDay(int year) {
        return Optional.ofNullable(
                isInaugurationYear(year) ? adjustForWeekends(INAUGURATION_DATE.atYear(year)) : null
        );
    }

    /**
     * Returns all Federal Holidays for the given calendar year.
     */
    public static SortedSet<LocalDate> observancesForYear(int year) {
        // supplies a TreeSet for collecting our holiday dates
        Supplier<SortedSet<LocalDate>> setSupplier = () -> new TreeSet<>(inaugurationDay(year)
                .map(Arrays::asList) // seed the TreeSet with a single-element collection containing inauguration day
                .orElseGet(ArrayList::new)); // or seed the TreeSet with an empty list when there's no inauguration day
        return Stream.of(values())
                .map(holiday -> holiday.forYear(year))
                .collect(Collectors.toCollection(setSupplier));
    }

    /**
     * Indicates if the given date is an observed Federal holiday, including inauguration days.
     *
     * @param date cannot be null
     * @return true only when the provided date is the observance of a Federal holiday, including inauguration days
     */
    public static boolean isObserved(LocalDate date) {
        requireNonNull(date);
        return IntStream.of(0, 1) // sometimes a date is a holiday in the subsequent year
                .map(i -> date.getYear() + i) // so look at holidays within both the current and subsequent year
                .mapToObj(FederalHoliday::observancesForYear)
                .flatMap(SortedSet::stream)
                .anyMatch(date::equals);
    }

    /**
     * Indicates if the given date falls on the weekend (i.e. is a Saturday or Sunday).
     *
     * @param date cannot be null
     * @return true if the given date is a Saturday or Sunday
     */
    public static boolean isWeekend(LocalDate date) {
        requireNonNull(date);
        return date.getDayOfWeek() == SUNDAY || date.getDayOfWeek() == SATURDAY;
    }

    /**
     * Indicates if the given date is a Saturday, Sunday, or an observed Federal holiday (Independence day and Labor day).
     * @param date cannot be null
     * @return true when the date is a weekend or observed Federal holiday, inauguration days included
     */
    public static boolean isNonWorkday(LocalDate date) {
        requireNonNull(date);
        return isWeekend(date) || isObserved(date);
    }
}