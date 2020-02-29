package cn.lwiki.job.engine;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;

/**
 * @author sweeter
 * @date 2020/02/29
 */
@Component
public class ScheduledJobTask extends ThreadPoolTaskScheduler {

    public ScheduledFuture<?> schedule(BaseTask baseTask, String expression){
       return this.schedule(baseTask, new CronTrigger(expression));
    }

    public ScheduledJobTask() {
        this.setPoolSize(20);
        this.setThreadNamePrefix("scheduledJobTask-");
        this.setWaitForTasksToCompleteOnShutdown(true);
        this.setAwaitTerminationSeconds(300);
        //初始化
        this.initialize();
    }
}
