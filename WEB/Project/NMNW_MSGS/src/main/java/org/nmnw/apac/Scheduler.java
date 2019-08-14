package org.nmnw.apac;

import org.nmnw.apac.amsa.Crawler;
import org.nmnw.apac.util.TimeHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

  private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

  private static final String EARLY_TIME = "0 0 08 * * *";
  private static final String LATE_TIME = "0 0 20 * * *";

  private Crawler crawler;


  @Scheduled(fixedDelay = 1000 * 60 * 10)
  public void HourScheduler() {
    logger.info("\t:: SERVER ALIVE {} ::", TimeHandle.getTime());
    //  test
    //crawler.crawl();
  }

  @Scheduled(cron = EARLY_TIME)
  public void earlyTimeSchedule() {
    logger.info("\t:: EARLY TIME SCHEDULE ACT START ::");
    crawler.crawl();
    logger.info("\t:: EARLY TIME SCHEDULE ACT END ::");
  }

  @Scheduled(cron = LATE_TIME)
  public void lateTimeSchedule() {
    logger.info("\t:: LATE TIME SCHEDULE ACT START ::");
    crawler.crawl();
    logger.info("\t:: LATE TIME SCHEDULE ACT END ::");
  }

}
