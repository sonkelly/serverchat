/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package allinone.tournement;

import static allinone.data.SimpleTable.mLog;
import allinone.tournement.TourProcessStep.TourProcessStarting;
import allinone.tournement.TourProcessStep.TourProcessWait;
import static java.lang.Thread.sleep;

import java.text.ParseException;
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
import vn.game.common.LoggerContext;

/**
 *
 * @author mac
 */
public class TourJob implements Job {

    private static Logger log = LoggerContext.getLoggerFactory()
            .getLogger(TourJob.class);
    private static boolean isInProgress = false;
    public static TourManager tourMgr;
    public static TournementEntity currentTour = null;

    public TournementEntity getTourMgr() {
        return currentTour;
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        if (!isInProgress) {
            isInProgress = true;
            try {
                mLog.debug("TourJob ");

                if (tourMgr == null) {
                    mLog.debug("khoi tao tour");
                    tourMgr = new TourManager();

                } else {
                    mLog.debug("new tour size:" + tourMgr.tours.size());
                    int tourSize = tourMgr.tours.size();
                    //lay tour day tien
                    for (int i = 0; i < tourSize; i++) {
                        currentTour = tourMgr.tours.get(i);
                        mLog.debug("tour.name:" + currentTour.name);
                        break;
                    }

                    if (currentTour != null) {
                        //wait aceept register tour
                        if (currentTour.status == TourStatus.TOUR_WAIT) {
                            TourProcessWait.process(tourMgr, currentTour);
                        }else if(currentTour.status == TourStatus.TOUR_STARTING){
                            TourProcessStarting.process(tourMgr, currentTour);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error(ex.getMessage(), ex);
            }

        }

        isInProgress = false;

    }
    //start test 
    private static String GAME_MONITOR_CRON_EXPRESSION = "0/30 * * * * ?";
    protected static SchedulerFactory sf = new StdSchedulerFactory();
    protected static Scheduler sched;

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

    private static void addJob(Class<? extends Job> jobClass, String identity, String cronExpression) throws ParseException, SchedulerException {
        JobDetail job = newJob(jobClass)
                .withIdentity(identity, "VIP")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(identity, "VIP")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        sched.scheduleJob(job, trigger);
    }

    public static void makeSchedule() {
        try {

            addJob(TourJob.class, "GAME_MONITOR", GAME_MONITOR_CRON_EXPRESSION);

            sched.start();

        } catch (ParseException ex) {
            System.out.println("parse error");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            System.out.println("SchedulerException error");
        }
    }

    public static void main(String[] args) throws Exception {

        TourJob.makeSchedule();
        sleep(3000);
        if (currentTour != null) {
            //wait aceept register tour
            String isRealMoney = "realmoney";
            if (currentTour.status == TourStatus.TOUR_WAIT) {
                TourTest.UserRegisterTour(tourMgr, 103, isRealMoney);
            }
        }else{
            System.out.println("ko co tour");
        }
    }
    //end test 
}
