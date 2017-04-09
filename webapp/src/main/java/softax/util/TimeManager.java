package softax.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class TimeManager {

    public Date getCurrentDateTime() {
        return Date.from(Instant.now());
    }

    public Date getCurrentDate() {
        return Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
    }

}
