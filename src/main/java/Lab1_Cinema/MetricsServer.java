package Lab1_Cinema;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MetricsServer {
    public static void start(PrometheusMeterRegistry registry) throws IOException {
        // Создаем сервер, привязываясь ко всем сетевым интерфейсам (0.0.0.0)
        // Это позволит Docker-контейнеру достучаться до приложения
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        server.createContext("/metrics", exchange -> {
            String response = registry.scrape();
            // Явно указываем тип контента, который ожидает Prometheus
            exchange.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4; charset=utf-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Metrics server started on http://0.0.0.0:8080/metrics");
    }
}
