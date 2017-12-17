package nl.cheelio.p1influxwriter.p1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractP1Reader implements P1Reader {
    protected BufferedReader reader;

    @Override
    public P1Message readNext() throws IOException {

        final List<String> lines = new ArrayList();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("/")) {
                lines.clear();
            }
            lines.add(line);
            if (line.startsWith("!")) {
                return new P1Message(lines);
            }
        }
        throw new IOException();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
