package cn.lwiki.job.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author sweeter
 * @date 2020/02/29
 */
@Slf4j
@RequestMapping("scheduleJob")
@RestController
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    @GetMapping("findList")
    public List<ScheduleJobEntity> findAllList() {
        return scheduleJobService.findAllList();
    }

    @GetMapping("start")
    public Object start(@RequestParam(required = false) Long jobId) {
        if (Objects.isNull(jobId)) {
          return scheduleJobService.startAll();
        }else {
            scheduleJobService.start(jobId);
        }

        return "成功";
    }

    @GetMapping("stop")
    public Object stop(@RequestParam(required = false) Long jobId) {
        if (Objects.isNull(jobId)) {
           return scheduleJobService.stopAll();
        }else {
            scheduleJobService.stop(jobId);
        }
        return "成功";
    }

    @GetMapping("update")
    public Object update(@RequestParam Long jobId, @RequestParam String cron) {
        scheduleJobService.update(jobId, cron);
        return "成功";
    }

    @GetMapping("reset")
    public Object reset(@RequestParam(required = false) Long jobId) {
        if (Objects.isNull(jobId)) {
            return scheduleJobService.resetAll();
        }else {
            scheduleJobService.reset(jobId);
        }
        return "重启成功";
    }
}
