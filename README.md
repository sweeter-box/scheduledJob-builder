# 定时任务


- 这是一个简单spring定时任务增强项目，初衷是将spring的定时任务信息持久化，方便在不修改代码的前提下修改cron表达式。

## 项目特点

- 采用ThreadPoolTaskScheduler可并行执行定时任务
- 动态修改cron表达式、启停任务
- 定时任务信息持久化（非整个任务持久化）
- web页面查看运行状态

  
 我们在spring架构的项目中常常使用@Scheduled注解来实现定时任务调度，非常方便、快捷，这也满足了我们的大多数项目需求。在最近的一个项目中，最开始也是使用@Scheduled注解来配置定时任务；但是由于定时任务越来越多已超过30个，还会时不时的调整cron表达式或者临时启停任务，调整起来就比较麻烦了，因此有了将定时任务信息持久化的想法，实现动态管理定时任务。

使用@Scheduled的不足：

- 无法动态修改cron表达式 ，必须修改代码重启项目；
- @Scheduled调度的定时任务是串行执行的，如果要并行还需要增加@EnableAsync和@Async来；


#### 常见的定时任务调度

- Timer(JDK) 不支持多线程
- ScheduledExecutorService(JDK)
- Quartz
- Spring Task
- ......
