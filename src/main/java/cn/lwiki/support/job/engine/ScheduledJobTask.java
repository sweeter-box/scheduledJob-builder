package cn.lwiki.support.job.engine;

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
        //线程池大小可根据实际情况修改，也可移动到统一配置类中集中管理
        this.setPoolSize(20);
        this.setThreadNamePrefix("scheduledJobTask-");
        this.setWaitForTasksToCompleteOnShutdown(true);
        this.setAwaitTerminationSeconds(300);
        //初始化
        this.initialize();
    }
}
