package applications;

import com.smartmirror.sys.view.AbstractSystemApplication;
import widgets.ClockWidget;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * System application - used for displaying time
 */
public class Clock extends AbstractSystemApplication{
    private ScheduledExecutorService scheduledExecutorService; // Used as timer

    /**
     * Will only run ones when application is started
     * Define application startup settings here
     *
     * SYSTEM_Screen functions as base JPanel for the application
     */
    public void setup()
    {
        setSYSTEM_Icon("img/clock-icon.png");   // Set application icon - will be displayed in settings

        SYSTEM_Widget = new ClockWidget();  // Instantiate corresponding widget

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(); // Instantiate Executor Service
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() { // Define runnable
            @Override
            public void run() {
                // Define stuff to update
                SYSTEM_Widget.init();
            }
        }, 0,1, TimeUnit.SECONDS); // Set timer
    }

    @Override
    public void init() {

    }

    @Override
    public void onBackButton() {
        SYSTEM_closeScreen();
    }
}
