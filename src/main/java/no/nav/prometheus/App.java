package no.nav.prometheus;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.common.TextFormat;
import io.prometheus.client.hotspot.DefaultExports;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.io.StringWriter;
import java.io.IOException;

public class App {
    public static Counter counter = Counter.build().name("antall_cver_lest").help("Antall cver lest").register();
    public static Histogram timeTaken = Histogram.build().name("tid_brukt_i_loop").help("Tid brukt i loop")
            .buckets(0.05, 0.1, 0.2).register();

    public static void main(String[] args) throws InterruptedException {
        DefaultExports.initialize();

        var vertx = Vertx.vertx();
        var router = Router.router(vertx);
        router.get("/ready").handler(routingContext -> {
            routingContext.response().end("OK");
        });
        router.get("/alive").handler(routingContext -> {
            routingContext.response().end("OK");
        });
        router.get("/metrics").handler(routingContext -> {
            var writer = new StringWriter();
            try {
                TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
            } catch (IOException ioEx) {
            }
            routingContext.response().end(writer.toString());
        });

        vertx.createHttpServer(new HttpServerOptions()).requestHandler(router).listen(8080);
        counter.inc(20d);
        counter.inc(20d);
        counter.inc(20d);
        counter.inc(20d);
        System.out.println("Started");
        for (int i = 0; i < 100000; i++) {
            var timer = timeTaken.startTimer();
            Thread.sleep(50);
            timer.observeDuration();
            System.out.print("*");
        }
    }
}
