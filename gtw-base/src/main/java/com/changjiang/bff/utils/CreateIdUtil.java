package com.changjiang.bff.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class CreateIdUtil {
    private final static Logger logger = LoggerFactory.getLogger(CreateIdUtil.class);

    /**
     * 雪花存储结构
     * (首位不存东西) + (4位时间序列) + (12位应用标识) + (10位每毫秒生成序列)
     */

    /**
     * 上一次生成时间
     */
    private static Long LAST_CREATE_TIME = 0L;

    /**
     * 每毫秒生成序列
     */
    private static Long MILLS_SEQ = 0L;

    /**
     * 每毫秒生成序列占位
     */
    private static Long MILLS_SEQ_BIT = 10L;

    /**
     * 毫秒内序列掩码，防止溢出
     */
    private static final Long MILLS_SEQ_MARK = -1L ^ (-1L << MILLS_SEQ_BIT);

    /**
     * 应用id
     */
    private static Long APP_ID = 1L;

    /**
     * 应用id占位
     */
    private static Long APP_ID_BIT = 12L;

    /**
     * 应用启动默认生成应用id
     */
    static {
        // 模拟的常见只在3000到4095这个范围段进行随机
        Integer maxAppId = 4095;
        Integer minAppId = 3000;
        Integer randomAppId = maxAppId - minAppId;
        Random random = new Random();
        // 根据最大值的范围生成一个随机数，然后再加上最小值组成一个appId
        Integer randomNum = random.nextInt(randomAppId);
        Long appId = Long.valueOf(randomNum + minAppId);
        APP_ID = appId << MILLS_SEQ_BIT;
        logger.info("系统默认随机分配应用号为: " + appId);
    }

    /**
     * 设置应用号
     * @param appId 应用号
     */
    public static void setAppId(long appId) {
        APP_ID = appId << MILLS_SEQ_BIT;
        logger.info("系统分配抢占应用号为: " + appId);
    }

    /**
     * 获取主键id
     * @return 主键id
     */
    public static synchronized Long nextId() {
        LAST_CREATE_TIME = nextTime();
        Long time = LAST_CREATE_TIME << (MILLS_SEQ_BIT + APP_ID_BIT);
        return time | APP_ID | MILLS_SEQ;
    }

    /**
     * 获取主键id, 返回String
     * @return 主键id的字符串形式
     */
    public static String nextIdToString() {
        return String.valueOf(nextId());
    }


    /**
     * 获取下一个毫秒级时间
     * 如果当前时间比上一次生成时间小，则阻塞直到能够拿到正确的时间，此处后续需要进行优化
     * @return {@link Long}
     */
    private static Long nextTime() {
        long currentTime = System.currentTimeMillis();
        //如果当前时间和上一生成时间一致则将每毫秒序列递增
        if (currentTime == LAST_CREATE_TIME) {
            MILLS_SEQ = (MILLS_SEQ + 1L) & MILLS_SEQ_MARK;
            //如果每毫秒序列回0，说明每毫秒能生成的id数已满，需要重新获取新的毫秒数
            if (MILLS_SEQ == 0L) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return nextTime();
            }
        } else if (currentTime < LAST_CREATE_TIME) {
            while (currentTime < LAST_CREATE_TIME) {
                currentTime = System.currentTimeMillis();
            }
        } else {
            MILLS_SEQ = 0L;
        }
        return currentTime;
    }
}
