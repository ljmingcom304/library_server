package com.ljming.timer.task;

import com.ljming.timer.TimerApplication;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Title:TimerJobHandler
 * <p>
 * Description:
 * </p >
 * Author Jming.L
 * Date 2022/3/30 16:02
 */
@Component
public class XxlJobHandler {

    Logger logger = LoggerFactory.getLogger(TimerApplication.class);


    @XxlJob("timerJobHandler")
    public void timerJobHandler() throws Exception {
        logger.info("执行定时任务==========>" + XxlJobHelper.getJobParam());
    }

}
