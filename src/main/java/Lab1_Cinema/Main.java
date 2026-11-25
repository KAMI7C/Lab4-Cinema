package Lab1_Cinema;

import Lab1_Cinema.repository.InMemoryTicketRepository;
import Lab1_Cinema.repository.TicketRepository;
import Lab1_Cinema.service.TicketService;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Создаем реестр Prometheus
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        // 2. Инициализируем зависимости
        TicketRepository repository = new InMemoryTicketRepository();
        TicketService service = new TicketService(repository, registry);

        // 3. Запускаем HTTP-сервер для метрик
        MetricsServer.start(registry);

        System.out.println("--- Приложение запущено. Начинаем имитацию покупок билетов ---");

        Random random = new Random();
        int seatCounter = 1;

        // 4. Бесконечный цикл для демонстрации работы (пока ты не нажмешь Stop)
        while (true) {
            try {
                int sessionId = random.nextInt(1, 4); // случайный сеанс от 1 до 3

                // Делаем вид, что пользователь пытается купить билет
                service.buyTicket(sessionId, seatCounter);

                // Иногда (каждый 3-й раз) пытаемся купить уже занятое место, чтобы вызвать ошибку (WARN)
                if (seatCounter % 3 == 0) {
                    service.buyTicket(sessionId, seatCounter);
                }

                seatCounter++;

                // Ждем 5 секунд перед следующей покупкой
                Thread.sleep(5000);

            } catch (Exception e) {
                // Если место занято, просто идем дальше, чтобы программа не падала
            }
        }
    }
}
