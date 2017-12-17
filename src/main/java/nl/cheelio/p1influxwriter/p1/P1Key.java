package nl.cheelio.p1influxwriter.p1;

public enum P1Key {

    VERSION("1-3:0.2.8", ReadingType.NONE),
    DATETIME("0-0:1.0.0", ReadingType.DATETIME),
    EQUIPMENT_IDENTIFIER("0-0:96.1.1", ReadingType.NONE),
    EQUIPMENT_IDENTIFIER_2("0-1:96.1.0", ReadingType.NONE),
    READING_ELECTRICITY_CONSUMED_TARIFF_1("1-0:1.8.1", ReadingType.KWH),
    READING_ELECTRICITY_CONSUMED_TARIFF_2("1-0:1.8.2", ReadingType.KWH),
    READING_ELECTRICITY_PRODUCED_TARIFF_1("1-0:2.8.1", ReadingType.KWH),
    READING_ELECTRICITY_PRODUCED_TARIFF_2("1-0:2.8.2", ReadingType.KWH),
    TARIFF_INDICATOR_ELECTRICITY("0-0:96.14.0", ReadingType.NONE),
    CURRENT_ELECTRICITY_CONSUMING("1-0:1.7.0", ReadingType.KW),
    CURRENT_ELECTRICITY_PRODUCING("1-0:2.7.0", ReadingType.KW),
    POWER_FAILURES_ANY_PHASE("0-0:96.7.21", ReadingType.NONE),
    LONG_POWER_FAILURES_ANY_PHASE("0-0:96.7.9", ReadingType.NONE),
    INSTANTANEOUS_CURRENT_L1("1-0:31.7.0", ReadingType.AMPERE),
    INSTANTANEOUS_ACTIVE_POWER_L1_POSITIVE("1-0:21.7.0", ReadingType.KW),
    INSTANTANEOUS_ACTIVE_POWER_L1_NEGATIVE("1-0:22.7.0", ReadingType.KW),
    DEVICE_TYPE("0-1:24.1.0", ReadingType.NONE),
    GAS_READING("0-1:24.2.1", ReadingType.M3),
    POWER_FAILURE_EVENT_LOG("1-0:99.97.0", ReadingType.EVENTLOG),
    UNKNOWN("", ReadingType.NONE);


    public ReadingType getReadingType() {
        return readingType;
    }

    public String getKey() {
        return key;
    }

    private final ReadingType readingType;
    private String key;

    P1Key(String key, ReadingType readingType) {
        this.key = key;
        this.readingType = readingType;

    }

    public static P1Key parse(String key) {
        for (P1Key p1Key : P1Key.values()) {
            if (p1Key.key.equals(key)) {
                return p1Key;
            }
        }
        return P1Key.UNKNOWN;
    }

    @Override
    public String toString() {
        return "P1Key{" +
                "readingType=" + readingType +
                ", key='" + key + '\'' +
                '}';
    }
}
