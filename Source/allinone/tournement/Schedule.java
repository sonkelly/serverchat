package allinone.tournement;

import java.text.ParseException;
import java.util.logging.Level;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schedule implements Job{
	protected static SchedulerFactory sf = new StdSchedulerFactory();
	protected static Scheduler sched;
	private static Logger log = LoggerFactory.getLogger(Schedule.class);
	private static String GAME_MONITOR_CRON_EXPRESSION = "0/1 * * * * ?";
	//private static String COMMON_MONITOR_CRON_EXPRESSION = "0/10 * * * * ?";

	static {
		try {
			sched = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException ex) {
			ex.printStackTrace();
		}
	}

	public static Scheduler getSchedule() {
		return sched;
	}

	private static void addJob(Class<? extends Job> jobClass, String identity,
			String cronExpression) throws ParseException, SchedulerException {
		JobDetail job = newJob(jobClass).withIdentity(identity, "VIP").build();

		Trigger trigger = newTrigger().withIdentity(identity, "VIP")
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();

		sched.scheduleJob(job, trigger);

	}

	public static void makeSchedule() {
//		try {
//                    try {
//                        addJob(TourManager.class, "TOUR_MONITOR",
//                                GAME_MONITOR_CRON_EXPRESSION);
//                    } catch (ParseException ex) {
//                        java.util.logging.Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//			sched.start();
//		} catch (SchedulerException ex) {
//			ex.printStackTrace();
//			log.error(ex.getMessage(), ex);
//		}

	}

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
