package com.tunnel.plc.fins;

import com.tunnel.platform.datacenter.config.MapCache;
import com.tunnel.plc.fins.mina.DevicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author yangqichao
 * @date 2019/9/17 13:52
 */
public class PlcProcess {

    private static final Logger logger = LoggerFactory.getLogger(PlcProcess.class);

    public static void shutdown() {
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    public void insertMlgDB() {
        List<String> plcList = CmdProcess.PlcList;
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        if (plcList.size() > 0) {
            for (String id : plcList) {
                if (MapCache.sessionCache.get(String.valueOf(id)) != null) {
                    MapCache.sessionCache.remove(String.valueOf(id), id);
                } else {
                    executorService.execute(new Task(id));
                }
            }
        }
        executorService.shutdown();//不能再提交新任务，等待执行的任务不受影响
        try {
            boolean loop = true;
            do {//等待所有任务完成
                loop = !executorService.awaitTermination(1, TimeUnit.SECONDS);  //阻塞，直到线程池里所有任务结束
            } while (loop);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程任务
     */
    public class Task implements Runnable {
        private String id;

        public Task(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            logger.debug("id:" + id);
            DevicesManager.getInstance().executeCommand(id);
        }


    }
}
