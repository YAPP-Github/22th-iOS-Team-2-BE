package com.pyonsnalcolor.batch.schedule;

import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.scheduling.annotation.Scheduled;

public abstract class Scheduler {
    private BatchService pbProductBatchService;
    private BatchService eventProductBatchService;

    public Scheduler(BatchService pbProductBatchService, BatchService eventProductBatchService) {
        this.pbProductBatchService = pbProductBatchService;
        this.eventProductBatchService = eventProductBatchService;
    }

    @Scheduled(cron = "0 0 6 * * SUN")
    public void run() {
        pbProductBatchService.execute();
        eventProductBatchService.execute();
    }
}
