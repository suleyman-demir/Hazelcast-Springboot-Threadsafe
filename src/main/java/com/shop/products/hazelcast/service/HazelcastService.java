package com.shop.products.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class HazelcastService {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastService( HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    public void executeRandomNumberTask() {
        IMap<Integer, Integer> numbers = hazelcastInstance.getMap("numbers");
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                Random random = new Random();
                int randomNumber = random.nextInt(10) + 1;



                numbers.lock(randomNumber);
                try {
                    numbers.put(randomNumber, numbers.getOrDefault(randomNumber, 0) + 1);
                } finally {
                    numbers.unlock(randomNumber);
                }
            });
        }


        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }


        numbers.forEach((key, value) -> System.out.println("Sayı: " + key + ", Adet: " + value));
        System.out.println("Üretilen Sayı Toplamı : "+numbers.values().stream().mapToInt(Integer::intValue).sum());
    }

}
