package org.nmnw.apac;

import org.nmnw.apac.amsa.Crawler;
import org.nmnw.apac.util.TimeHandleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

  private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

  private static final String EARLY_TIME = "0 10 09 * * *";
  private static final String LATE_TIME = "0 0 20 * * *";


  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void HourScheduler() {
    logger.info("\t:: SERVER ALIVE {} ::", TimeHandleUtil.getTime());
  }

  @Scheduled(cron = EARLY_TIME)
  public void earlyTimeSchedule() {
    logger.info("\t:: EARLY TIME SCHEDULE ACT START ::");
    Crawler.crawl();
    logger.info("\t:: EARLY TIME SCHEDULE ACT END ::");
  }

  @Scheduled(cron = LATE_TIME)
  public void lateTimeSchedule() {
    logger.info("\t:: LATE TIME SCHEDULE ACT START ::");
    Crawler.crawl();
    logger.info("\t:: LATE TIME SCHEDULE ACT END ::");
  }

}
