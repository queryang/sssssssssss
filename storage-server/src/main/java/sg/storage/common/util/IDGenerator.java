package sg.storage.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * id生成器
 */
public class IDGenerator {

    private final static SnowflakeIdWorker DEFAULT_STATIC_SNOWFLAKEIDWORKER = new SnowflakeIdWorker(0, 0);

    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private static AtomicInteger number_prefix = new AtomicInteger(0);

    private static volatile Long roundMillis = 0L;

    /**
     * uuid方式生成32位ID
     *
     * @return
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static String id(int size) {
        Random random = new Random();
        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = digits[random.nextInt(digits.length)];
        }
        return new String(cs);
    }

    /**
     * 当前时间+雪花算法生成ID
     *
     * @return
     */
    public static Long jobId() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        long snowflakeId = idWorker.nextId();
        String jobId = date + snowflakeId;
        return Long.parseLong(jobId);
    }

    /**
     * 雪花算法生成ID (18)
     *
     * @return
     */
    public static Long snowflakeId() {
        long snowflakeId = DEFAULT_STATIC_SNOWFLAKEIDWORKER.nextId();
        return snowflakeId;
    }

    /**
     * 生成16位数字ID（不适用于分布式环境，和高并发环境）
     *
     * @return
     */
    public static Long number16Id() throws InterruptedException {
        String prefix = getNumber();
        return Long.parseLong(prefix);
    }

    public static synchronized String getNumber() throws InterruptedException {
        if (number_prefix.get() > 999) {
            if (roundMillis == System.currentTimeMillis()) {
                //如果循环则睡眠一毫秒，防止统一毫秒内数字重复
                Thread.sleep(1);
            }
            number_prefix.set(0);
        }
        int number = number_prefix.getAndIncrement();
        if (number == 0) {
            //如果为循环开头则记录当前毫秒数
            roundMillis = System.currentTimeMillis();
        }
        String prefix = number + "";
        String zeroStr = "";
        if (prefix.length() < 3) {
            for (int i = 0; i < 3 - prefix.length(); i++) {
                zeroStr += "0";
            }
        }
        return roundMillis + zeroStr + prefix;
    }

    /**
     * 默认ID生成，使用uuid
     *
     * @return
     */
    public static String getId() {
        return uuid();
    }

    /**
     * 雪花算法生成ID
     *
     * @return
     */
    public static Long getSnowflakeId() {
        return snowflakeId();
    }

    /**
     * 当前时间+雪花算法生成ID
     *
     * @return
     */
    public static Long getJobId() {
        return jobId();
    }
}