package sg.storage.common.util;

import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

    public static boolean compare(String param1, String param2) {
        if (StringUtils.isBlank(param1) && StringUtils.isBlank(param2)) {
            return true;
        }
        if (StringUtils.isBlank(param1) || StringUtils.isBlank(param2)) {
            return false;
        }
        return param1.equals(param2);
    }

    public static boolean compare(Integer param1, Integer param2) {
        if (null == param1 && null == param2) {
            return true;
        }
        if (null == param1 || null == param2) {
            return false;
        }
        return param1.equals(param2);
    }
}