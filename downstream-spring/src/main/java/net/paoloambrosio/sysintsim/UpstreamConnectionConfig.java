package net.paoloambrosio.sysintsim;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(prefix="service.upstream")
public class UpstreamConnectionConfig {

    @Value("${service.upstream.url}")
    private String url;

    @Value("${service.upstream.pool-size}")
    private int poolSize;

    @Value("${service.upstream.socket-timeout}")
    private int socketTimeout;

    //@Value("${service.upstream.tcp-no-delay}")
    private boolean tcpNoDelay; // Disable Nagle

    public String getUrl() {
        return url;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }
}
