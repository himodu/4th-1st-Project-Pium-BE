package gdg.pium.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter DotFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd:HH.mm.ss");

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DotFormatter);
    }
}
