package net.paoloambrosio.sysintsim;

import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.SocketTimeoutException;

@RestController
public class DownstreamController {

    private final Executor executor;
    private final String upstreamUrl;

    @Autowired
    public DownstreamController(UpstreamConnectionConfig ucc) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(ucc.getPoolSize());
        cm.setDefaultMaxPerRoute(ucc.getPoolSize());
        cm.setDefaultSocketConfig(SocketConfig.custom()
                .setSoKeepAlive(true)
                .setSoTimeout(ucc.getSocketTimeout())
                .setTcpNoDelay(ucc.isTcpNoDelay())
                .build()
            );
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build();
        executor = Executor.newInstance(client);
        upstreamUrl = ucc.getUrl();
    }

    @RequestMapping("/")
    public ResponseEntity<String> index() {
        try {
            String upstreamResponse = executor.execute(Request.Get(upstreamUrl)).returnContent().asString();
            HttpStatus status = "success".equals(upstreamResponse) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
            return new ResponseEntity<String>("success-" + upstreamResponse, status);
        } catch (SocketTimeoutException e) {
            return new ResponseEntity<String>("failure-timeout", HttpStatus.GATEWAY_TIMEOUT);
        } catch (IOException e) {
            return new ResponseEntity<String>("failure-unknown", HttpStatus.GATEWAY_TIMEOUT);
        }
    }

}
