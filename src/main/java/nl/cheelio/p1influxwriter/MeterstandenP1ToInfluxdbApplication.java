package nl.cheelio.p1influxwriter;

import nl.cheelio.p1influxwriter.p1.P1Message;
import nl.cheelio.p1influxwriter.p1.P1Reader;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MeterstandenP1ToInfluxdbApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);
    private InfluxDB influxDB;

    @Value("${influxdb.hostname}")
    private String influxDbHostname;
    @Value("${influxdb.port}")
    private int influxDbPort;
    @Value("${influxdb.username}")
    private String influxDbUsername;
    @Value("${influxdb.password}")
    private String influxDbPassword;
    @Value("${influxdb.database}")
    private String influxDbDatabase;

    @Autowired
    private P1Reader reader;

    @PostConstruct
    void init() {
        influxDB = InfluxDBFactory
                .connect(String.format("http://%s:%s", influxDbHostname, influxDbPort), influxDbUsername, influxDbPassword)
                .setDatabase(influxDbDatabase);
        influxDB.disableBatch();

    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(MeterstandenP1ToInfluxdbApplication.class)
                .logStartupInfo(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Override
    public void run(final String... args) throws Exception {
        logger.info("Start reading P1 messages...");

        P1Message p1Message;
        while ((p1Message = reader.readNext()) != null) {
            Point point = messageToPoint(p1Message);

            influxDB.write(point);
            logger.info("Sent message: {}", point);
        }
    }

    private Point  messageToPoint(final P1Message p1Message) {
        return Point.measurement("reading")
                .time(p1Message.getDateTime() == null ? LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() : p1Message.getDateTime().toInstant(ZoneOffset.UTC).toEpochMilli(), TimeUnit.MILLISECONDS)
                .addField("electricity.current.consuming", p1Message.getCurrentElectricityConsuming())
                .addField("electricity.current.producing", p1Message.getCurrentElectricityProducing())
                .addField("electricity.current.tariff", p1Message.getCurrentTariff())
                .addField("electricity.total.consumed.tariff.1", p1Message.getElectricityConsumedTariff1())
                .addField("electricity.total.consumed.tariff.2", p1Message.getElectricityConsumedTariff2())
                .addField("electricity.total.produced.tariff.1", p1Message.getElectricityProducedTariff1())
                .addField("electricity.total.produced.tariff.2", p1Message.getElectricityProducedTariff2())
                .addField("gas.total.consumed", p1Message.getGasConsumed())
                .addField("electricity.instantaneous.active.power.l1.negative", p1Message.getInstantaneousActivePowerL1Negative())
                .addField("electricity.instantaneous.active.power.l1.positive", p1Message.getInstantaneousActivePowerL1Positive())
                .addField("electricity.instantaneous.current.l1", p1Message.getInstantaneousCurrentL1())
                .addField("electricity.failures.long", p1Message.getNumLongPowerFailuresAnyPhase())
                .addField("electricity.failures", p1Message.getNumPowerFailuresAnyPhase())
                .build();
    }
}
