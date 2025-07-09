package com.admiral.onlineshop.integration.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class TestHelper {

    /**
     * Одновременный запуск(в ту же миллисекунду)
     *
     * @param runnable параллельные задачи
     *
     * @throws InterruptedException
     */
    public void runSynchronously(Runnable... runnable) throws InterruptedException {
        int threads = runnable.length;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch readyLatch = new CountDownLatch(threads);     // Подсчет готовности
        CountDownLatch startLatch = new CountDownLatch(1);           // Старт по сигналу
        CountDownLatch doneLatch = new CountDownLatch(threads);      // Ожидание завершения
        for (int i = 0; i <= threads; i++) {
            int taskId = i;
            int finalI = i;
            executor.submit(() -> {
                try {
                    log.info("Task {} ready.", taskId);
                    readyLatch.countDown();        // Задача готова

                    startLatch.await();            // Ждем сигнала на старт

                    // Основная логика
                    log.info("Task {} started at {}",
                            taskId,
                            System.currentTimeMillis());
                    runnable[finalI].run();
                    log.info("Task {} finished.", taskId);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();         // Задача завершена
                }
            });
        }
        readyLatch.await(); // Ждем, пока оба потока будут готовы
        log.info(">>> All tasks ready. Starting simultaneously.");
        startLatch.countDown(); // Даем сигнал на старт
        doneLatch.await(); // Ждем завершения всех

        executor.shutdown();
        log.info(">>> All tasks completed.");
    }
}
