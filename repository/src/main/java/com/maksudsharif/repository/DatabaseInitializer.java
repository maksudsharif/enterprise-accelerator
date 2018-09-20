package com.maksudsharif.repository;

import com.maksudsharif.repository.model.Record;
import com.maksudsharif.repository.model.RecordRepository;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Profile("generate")
@Component
@Getter
@Log4j2
public class DatabaseInitializer implements CommandLineRunner
{
    private RecordRepository recordRepository;

    public DatabaseInitializer(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public void run(String... strings) throws Exception
    {
        //createRecords();
        long count = recordRepository.count();
        int PAGE=0,PAGE_SIZE=100;
        Page<Record> recordPage;
        do {
            recordPage = recordRepository.findAll(PageRequest.of(PAGE, PAGE_SIZE));
            log.info("Retrieved page: [{}]", recordPage);
        } while (recordPage.hasNext());
    }

    private void createRecords()
    {
        final RandomStringGenerator build = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
        log.info("Initializing development database...");
        StopWatch watch = new StopWatch();
        watch.start();
        CompletableFuture.allOf(Stream.generate(UUID::randomUUID).limit(100)
                .map(ignored -> CompletableFuture.supplyAsync(() -> {
                    try
                    {
                        String generated = DigestUtils.sha512Hex(build.generate(512));
                        Record record = new Record(generated);
                        getRecordRepository().save(record);
                        log.info(String.format("Warming cache for id: [{%s}]", record.getId()));
                        getRecordRepository().findById(record.getId());
                        log.info(String.format("Generated data:\t[%s]", record));
                        return true;
                    } catch (Exception e) {
                        log.error("Error", e);
                        throw e;
                    }
                })).toArray(CompletableFuture[]::new)).handleAsync((aVoid, throwable) -> {
                    watch.stop();
                    log.info(String.format("Database initialization complete in ... \n[{%s}]", watch.prettyPrint()));
                    return null;
                });
    }
}
