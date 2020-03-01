package cn.lwiki.job.engine;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @date 2020/02/29
 * 定时任务持久化配置实体
 */
@Entity
@Table(name = "scheduleJob")
@Data
@EntityListeners({AuditingEntityListener.class})
public class ScheduleJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 定时任务标题
     */
    private String title;
    /**
     * 类名
     */
    @Column(nullable = false,unique = true)
    private String className;
    /**
     * cron表达式
     */
    @Column(nullable = false)
    private String cron;
    /**
     * 状态: 0 停用 1 启动
     */
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private EnableStatus status;

    private String description;
    /**
     * 最后一次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastExeTime;
    /**
     * 下一次执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime nextExeTime;
    /**
     * 业务执行状态 1:成功 0:失败
     */
    @Enumerated(EnumType.ORDINAL)
    private BusinessStatus lastBusinessStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @CreatedDate
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Transient
    private RunStatus runStatus;
}
