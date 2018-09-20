package com.maksudsharif.repository;

import com.maksudsharif.repository.model.Record;
import com.maksudsharif.repository.model.RecordRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
@Getter
@Log4j2
@Profile("periodic")
public class ScheduledDataLoader
{
    private final RecordRepository repository;

    public ScheduledDataLoader(RecordRepository repository)
    {
        this.repository = repository;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void periodicDataLoader()
    {
        final RandomStringGenerator build = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        log.debug("Starting Periodic Data load...");
        List<Long> times = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture.allOf(Stream.generate(UUID::randomUUID).limit(100)
                .map(ignored -> CompletableFuture.supplyAsync(() -> {
                    try
                    {
                        String generated = DigestUtils.sha512Hex(build.generate(512));
                        Record record = new Record(generated);

                        StopWatch watch = new StopWatch();
                        watch.start();
                        getRepository().save(record);
                        watch.stop();
                        times.add(watch.getTotalTimeMillis());
                        getRepository().findById(record.getId());
                        getRepository().findById(record.getId());
                        return true;
                    } catch (Exception e) {
                        log.error("Error", e);
                        throw e;
                    }
                }))
                .toArray(CompletableFuture[]::new))
                .handleAsync((aVoid, throwable) -> {
                    log.info("avg time for write: \t[{}ms]", times.stream().mapToDouble(a -> a).average().getAsDouble());

                    return null;
                });
    }
}
