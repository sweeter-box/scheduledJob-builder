package cn.lwiki.support.job.example;

import cn.lwiki.support.job.engine.EnableStatus;
import cn.lwiki.support.job.engine.ScheduleJobEntity;
import cn.lwiki.support.job.engine.ScheduleJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author sweeter
 * @date 2020/2/29
 */
@Lazy(false)
@Component
public class InitTask {

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    @PostConstruct
    public void init() {
        ScheduleJobEntity entity = new ScheduleJobEntity();
        entity.setId(1L);
        entity.setTitle("测试定时任务");
        entity.setCron("0/1000 * *  * * ? ");
        entity.setDescription("这是一个描述");
        entity.setClassName("cn.lwiki.support.job.example.DemoTask");
        entity.setStatus(EnableStatus.ENABLE);

        List<ScheduleJobEntity> jobList = Arrays.asList(entity);
        scheduleJobRepository.saveAll(jobList);

    }
}
