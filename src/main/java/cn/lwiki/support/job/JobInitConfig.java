package cn.lwiki.support.job;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableJpaRepositories("cn.lwiki.support.job")
@EntityScan("cn.lwiki.support.job")
public class JobInitConfig {


}
