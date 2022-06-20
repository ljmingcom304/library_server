package com.ljming.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class TimerApplication {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TimerApplication.class);
        logger.info("[" + TimerApplication.class.getSimpleName() + "开始启动]" + Arrays.toString(args));
        SpringApplication.run(TimerApplication.class, args);
    }

}
