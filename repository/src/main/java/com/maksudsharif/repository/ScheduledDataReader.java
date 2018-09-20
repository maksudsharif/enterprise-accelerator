package com.maksudsharif.repository;

import com.maksudsharif.repository.model.RecordRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

@Component
@Getter
@Log4j2
@Profile("read_periodic")
public class ScheduledDataReader
{
    private final RecordRepository repository;

    public ScheduledDataReader(RecordRepository repository)
    {
        this.repository = repository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void periodicDataLoader()
    {
        log.debug("Starting read");
        List<Long> times = new ArrayList<>();
        long total = repository.count();
        long randomStart = ThreadLocalRandom.current().nextLong(0, total);
        LongStream.range(randomStart, randomStart + 1000).forEach(id -> {
            StopWatch watch = new StopWatch();
            watch.start();
            repository.findById(id);
            watch.stop();
            log.debug("Find took: [{}ms]", watch.getTotalTimeMillis());
            times.add(watch.getTotalTimeMillis());
        });

        log.info("avg time for read: \t[{}ms]", times.stream().mapToDouble(a -> a).average().getAsDouble());
    }
}
