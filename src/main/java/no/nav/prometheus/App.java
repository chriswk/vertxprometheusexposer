package no.nav.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.hotspot.DefaultExports;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

public class App {
    public static Counter counter = Counter.build("myname", "help").register();

    public static void main(String[] args) {
        DefaultExports.initialize();
        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(new MicrometerMetricsOptions()
                .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true).setStartEmbeddedServer(true)
                        .setEmbeddedServerOptions(new HttpServerOptions().setPort(8080))
                        .setEmbeddedServerEndpoint("/metrics").setPublishQuantiles(true))
                .setEnabled(true).setJvmMetricsEnabled(true)));
        counter.inc(20d);
        counter.inc(20d);
        counter.inc(20d);
        counter.inc(20d);
        System.out.println("Started");
    }
}
