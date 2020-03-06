package cn.lwiki.support.job.engine;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * @author sweeter
 * @date 2020/2/29
 */
public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long> {
    /**
     * 按启用状态查询
     * @param status
     * @return
     */
    List<ScheduleJobEntity> findListByStatus(EnableStatus status);
}
