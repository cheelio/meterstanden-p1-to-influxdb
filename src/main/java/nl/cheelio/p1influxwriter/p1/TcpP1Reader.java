package nl.cheelio.p1influxwriter.p1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

@Profile("tcp")
@Component
public class TcpP1Reader extends AbstractP1Reader implements P1Reader, AutoCloseable {

    public TcpP1Reader(@Value("${tcp.host}") final String ipAddress, @Value("${tcp.port}") final int port) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(new Socket(ipAddress, port).getInputStream()));
    }


}
