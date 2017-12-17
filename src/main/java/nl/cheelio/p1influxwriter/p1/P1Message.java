package nl.cheelio.p1influxwriter.p1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class P1Message {

    private static final Pattern keyPattern = Pattern.compile("(\\d*?-\\d*?:\\d*?\\.\\d*?\\.\\d*?)\\(");
    private static final Pattern valuePattern = Pattern.compile("\\((.*?)\\)");
    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private Double gasConsumed;
    private LocalDateTime dateTimeGasMeterReading;
    private String deviceType;

    private String identifier1;
    private String identifier2;
    private String version;

    private LocalDateTime dateTime;
    private Double instantaneousActivePowerL1Positive;
    private Double instantaneousCurrentL1;
    private Double instantaneousActivePowerL1Negative;
    private int numLongPowerFailuresAnyPhase;
    private int numPowerFailuresAnyPhase;
    private Double currentElectricityProducing;
    private Double currentElectricityConsuming;
    private int currentTariff;

    private Double electricityConsumedTariff1;
    private Double electricityConsumedTariff2;
    private Double electricityProducedTariff1;
    private Double electricityProducedTariff2;
    private List<PowerFailure> powerFailureHistory;

    public P1Message(final List<String> lines) {

        lines.stream()
                .filter(line -> !line.startsWith("/") && !line.startsWith("!") && !line.isEmpty())
                .forEach(line -> {

                    final Matcher keyMatcher = keyPattern.matcher(line);
                    final Matcher valueMatcher = valuePattern.matcher(line);

                    while (keyMatcher.find()) {
                        final P1Key key = P1Key.parse(keyMatcher.group(1));
                        valueMatcher.find();
                        final String match = valueMatcher.group(1);
                        switch (key) {
                            case VERSION:
                                this.version = match;
                                break;
                            case DATETIME:
                                this.dateTime = parseDateTime(match);
                                break;
                            case EQUIPMENT_IDENTIFIER:
                                this.identifier1 = match;
                                break;
                            case EQUIPMENT_IDENTIFIER_2:
                                this.identifier2 = match;
                                break;
                            case READING_ELECTRICITY_CONSUMED_TARIFF_1:
                                this.electricityConsumedTariff1 = parseDouble(match);
                                break;
                            case READING_ELECTRICITY_CONSUMED_TARIFF_2:
                                this.electricityConsumedTariff2 = parseDouble(match);
                                break;
                            case READING_ELECTRICITY_PRODUCED_TARIFF_1:
                                this.electricityProducedTariff1 = parseDouble(match);
                                break;
                            case READING_ELECTRICITY_PRODUCED_TARIFF_2:
                                this.electricityProducedTariff2 = parseDouble(match);
                                break;
                            case TARIFF_INDICATOR_ELECTRICITY:
                                this.currentTariff = Integer.parseInt(match);
                                break;
                            case CURRENT_ELECTRICITY_CONSUMING:
                                this.currentElectricityConsuming = parseDouble(match);
                                break;
                            case CURRENT_ELECTRICITY_PRODUCING:
                                this.currentElectricityProducing = parseDouble(match);
                                break;
                            case POWER_FAILURES_ANY_PHASE:
                                this.numPowerFailuresAnyPhase = Integer.parseInt(match);
                                break;
                            case LONG_POWER_FAILURES_ANY_PHASE:
                                this.numLongPowerFailuresAnyPhase = Integer.parseInt(match);
                                break;
                            case INSTANTANEOUS_CURRENT_L1:
                                this.instantaneousCurrentL1 = parseDouble(match);
                                break;
                            case INSTANTANEOUS_ACTIVE_POWER_L1_POSITIVE:
                                this.instantaneousActivePowerL1Positive = parseDouble(match);
                                break;
                            case INSTANTANEOUS_ACTIVE_POWER_L1_NEGATIVE:
                                this.instantaneousActivePowerL1Negative = parseDouble(match);
                                break;
                            case DEVICE_TYPE:
                                this.deviceType = match;
                                break;
                            case GAS_READING:
                                this.dateTimeGasMeterReading = parseDateTime(match);
                                valueMatcher.find();
                                this.gasConsumed = parseDouble(valueMatcher.group(1));
                                break;
                            case POWER_FAILURE_EVENT_LOG:
                                this.powerFailureHistory = new ArrayList<>();
                                valueMatcher.find();
                                while (valueMatcher.find()) {
                                    final LocalDateTime dateTime = parseDateTime(valueMatcher.group(1));
                                    valueMatcher.find();
                                    final int duration = parseInteger(valueMatcher.group(1));
                                    this.powerFailureHistory.add(new PowerFailure(dateTime, duration));
                                }
                                break;
                            case UNKNOWN:
                                break;
                        }
                    }
                });

    }

    private LocalDateTime parseDateTime(final String firstGroup) {
        return LocalDateTime.parse(firstGroup.replaceAll("W", "").replaceAll("S", ""), dateTimeFormat);
    }

    private double parseDouble(final String reading) {
        try {
            return Double.parseDouble(reading.substring(0, reading.indexOf("*")));
        } catch (Exception e) {
            return 0.0D;
        }
    }

    private int parseInteger(final String reading) {
        try {
            return Integer.parseInt(reading.substring(0, reading.indexOf("*")));
        } catch (Exception e) {
            return 0;
        }
    }

    public Double getGasConsumed() {
        return gasConsumed;
    }

    public void setGasConsumed(Double gasConsumed) {
        this.gasConsumed = gasConsumed;
    }

    public LocalDateTime getDateTimeGasMeterReading() {
        return dateTimeGasMeterReading;
    }

    public void setDateTimeGasMeterReading(LocalDateTime dateTimeGasMeterReading) {
        this.dateTimeGasMeterReading = dateTimeGasMeterReading;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getIdentifier1() {
        return identifier1;
    }

    public void setIdentifier1(String identifier1) {
        this.identifier1 = identifier1;
    }

    public String getIdentifier2() {
        return identifier2;
    }

    public void setIdentifier2(String identifier2) {
        this.identifier2 = identifier2;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getInstantaneousActivePowerL1Positive() {
        return instantaneousActivePowerL1Positive;
    }

    public void setInstantaneousActivePowerL1Positive(Double instantaneousActivePowerL1Positive) {
        this.instantaneousActivePowerL1Positive = instantaneousActivePowerL1Positive;
    }

    public Double getInstantaneousCurrentL1() {
        return instantaneousCurrentL1;
    }

    public void setInstantaneousCurrentL1(Double instantaneousCurrentL1) {
        this.instantaneousCurrentL1 = instantaneousCurrentL1;
    }

    public Double getInstantaneousActivePowerL1Negative() {
        return instantaneousActivePowerL1Negative;
    }

    public void setInstantaneousActivePowerL1Negative(Double instantaneousActivePowerL1Negative) {
        this.instantaneousActivePowerL1Negative = instantaneousActivePowerL1Negative;
    }

    public int getNumLongPowerFailuresAnyPhase() {
        return numLongPowerFailuresAnyPhase;
    }

    public void setNumLongPowerFailuresAnyPhase(int numLongPowerFailuresAnyPhase) {
        this.numLongPowerFailuresAnyPhase = numLongPowerFailuresAnyPhase;
    }

    public int getNumPowerFailuresAnyPhase() {
        return numPowerFailuresAnyPhase;
    }

    public void setNumPowerFailuresAnyPhase(int numPowerFailuresAnyPhase) {
        this.numPowerFailuresAnyPhase = numPowerFailuresAnyPhase;
    }

    public Double getCurrentElectricityProducing() {
        return currentElectricityProducing;
    }

    public void setCurrentElectricityProducing(Double currentElectricityProducing) {
        this.currentElectricityProducing = currentElectricityProducing;
    }

    public Double getCurrentElectricityConsuming() {
        return currentElectricityConsuming;
    }

    public void setCurrentElectricityConsuming(Double currentElectricityConsuming) {
        this.currentElectricityConsuming = currentElectricityConsuming;
    }

    public int getCurrentTariff() {
        return currentTariff;
    }

    public void setCurrentTariff(int currentTariff) {
        this.currentTariff = currentTariff;
    }

    public Double getElectricityConsumedTariff1() {
        return electricityConsumedTariff1;
    }

    public void setElectricityConsumedTariff1(Double electricityConsumedTariff1) {
        this.electricityConsumedTariff1 = electricityConsumedTariff1;
    }

    public Double getElectricityConsumedTariff2() {
        return electricityConsumedTariff2;
    }

    public void setElectricityConsumedTariff2(Double electricityConsumedTariff2) {
        this.electricityConsumedTariff2 = electricityConsumedTariff2;
    }

    public Double getElectricityProducedTariff1() {
        return electricityProducedTariff1;
    }

    public void setElectricityProducedTariff1(Double electricityProducedTariff1) {
        this.electricityProducedTariff1 = electricityProducedTariff1;
    }

    public Double getElectricityProducedTariff2() {
        return electricityProducedTariff2;
    }

    public void setElectricityProducedTariff2(Double electricityProducedTariff2) {
        this.electricityProducedTariff2 = electricityProducedTariff2;
    }

    public List<PowerFailure> getPowerFailureHistory() {
        return powerFailureHistory;
    }

    public void setPowerFailureHistory(List<PowerFailure> powerFailureHistory) {
        this.powerFailureHistory = powerFailureHistory;
    }

    @Override
    public String toString() {
        return "P1Message{" +
                "powerFailureHistory=" + powerFailureHistory +
                ", gasConsumed=" + gasConsumed +
                ", dateTimeGasMeterReading=" + dateTimeGasMeterReading +
                ", deviceType='" + deviceType + '\'' +
                ", identifier1='" + identifier1 + '\'' +
                ", identifier2='" + identifier2 + '\'' +
                ", version='" + version + '\'' +
                ", dateTime=" + dateTime +
                ", instantaneousActivePowerL1Positive=" + instantaneousActivePowerL1Positive +
                ", instantaneousCurrentL1=" + instantaneousCurrentL1 +
                ", instantaneousActivePowerL1Negative=" + instantaneousActivePowerL1Negative +
                ", numLongPowerFailuresAnyPhase=" + numLongPowerFailuresAnyPhase +
                ", numPowerFailuresAnyPhase=" + numPowerFailuresAnyPhase +
                ", currentElectricityProducing=" + currentElectricityProducing +
                ", currentElectricityConsuming=" + currentElectricityConsuming +
                ", currentTariff='" + currentTariff + '\'' +
                ", electricityConsumedTariff1=" + electricityConsumedTariff1 +
                ", electricityConsumedTariff2=" + electricityConsumedTariff2 +
                ", electricityProducedTariff1=" + electricityProducedTariff1 +
                ", electricityProducedTariff2=" + electricityProducedTariff2 +
                '}';
    }
}
