package com.pyonsnalcolor.batch.schedule.gs25;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GS25ProductScheduler extends Scheduler {
    @Autowired
    public GS25ProductScheduler(@Qualifier("GS25Pb") BatchService gs25PbBatchService,
                                @Qualifier("GS25Event") BatchService gs25EventBatchService) {
        super(gs25PbBatchService, gs25EventBatchService);
    }
}
