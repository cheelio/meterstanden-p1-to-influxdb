package nl.cheelio.p1influxwriter.p1;

import java.io.IOException;

public interface P1Reader {

    P1Message readNext() throws IOException;

    void close() throws IOException;

}

