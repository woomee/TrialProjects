package com.example.demo.jobs;

import java.util.logging.Logger;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.example.demo.DemoApplication;

@Component
public class Tasklet2_2 implements Tasklet{
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
      Logger.getLogger(DemoApplication.class.getName()).info("Tasklet2_2 を実行します。");

      // 1分のスリープ
      Thread.sleep(1000*60);

      Logger.getLogger(DemoApplication.class.getName()).info("Tasklet2_2 が実行されました。");

      return RepeatStatus.FINISHED;
    }
}
