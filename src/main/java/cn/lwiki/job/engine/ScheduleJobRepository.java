package cn.lwiki.job.engine;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author sweeter
 * @date 2020/2/29
 */
public interface ScheduleJobRepository extends JpaRepository<ScheduleJobEntity, Long> {

    /**
     * 查询所有启用状态的任务
     * @return
     */
    @Query(nativeQuery=true,value ="select * from scheduleJob where status = 1")
    List<ScheduleJobEntity> findAllEnableList();
}
