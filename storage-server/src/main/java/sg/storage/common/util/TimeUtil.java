package sg.storage.common.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter DAYFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter SECONDFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间（毫秒）
     *
     * @return
     */
    public static Long getCurrentTime() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前日期（yyyy-MM-dd HH:mm:ss）
     *
     * @return
     */
    public static String getCurrentDate() {
        return LocalDateTime.now(ZoneOffset.of("+8")).format(SECONDFORMATTER);
    }

    /**
     * 时间（毫秒）转日期（yyyy-MM-dd HH:mm:ss）
     *
     * @param time
     * @return
     */
    public static String timeToDate(Long time) {
        return LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8)).format(SECONDFORMATTER);
    }
}