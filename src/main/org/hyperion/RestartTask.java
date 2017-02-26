package org.hyperion;

import org.hyperion.util.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by Gilles on 17/12/2015.
 */
public class RestartTask extends TimerTask {
    private static final Calendar RESTART_TIME = Calendar.getInstance();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd/MM");

    public static void submitRestartTask() {
        Timer timer = new Timer();
        RESTART_TIME.set(Calendar.HOUR_OF_DAY, 3);
        RESTART_TIME.set(Calendar.MINUTE, 0);
        RESTART_TIME.set(Calendar.SECOND, 0);
        if(System.currentTimeMillis() > RESTART_TIME.getTimeInMillis()){
            RESTART_TIME.add(Calendar.DATE, 1);
        }
        timer.schedule(
                new RestartTask(),
                RESTART_TIME.getTime(),
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        );
        if(Configuration.getBoolean(Configuration.ConfigurationObject.DEBUG))
            Server.getLogger().log(Level.INFO, "Restart task successfully submitted. Restart will occur: '" + DATE_FORMAT.format(new Date(RESTART_TIME.getTimeInMillis())) + "'");
    }

    @Override
    public void run() {
        if (Server.getUptime().millisUptime() > (Time.ONE_HOUR * 5)) {
            System.out.println("Daily restart task submitted.");
            Server.update(120, "Automatic daily restart.");
        } else submitRestartTask();
    }
}
