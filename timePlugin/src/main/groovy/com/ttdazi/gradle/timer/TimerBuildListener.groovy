package com.ttdazi.gradle.timer

import com.ttdazi.gradle.model.Clock;
import org.gradle.BuildListener;
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

import java.util.concurrent.ConcurrentHashMap;


/**
 * author: Y_Qing
 * date: 2019/12/16
 */
 class TimerBuildListener implements TaskExecutionListener, BuildListener {
     private clocks = new ConcurrentHashMap()
     private static final DISPLAY_TIME_THRESHOLD=50
     private times = []

     @Override
     void buildStarted(Gradle gradle) {

     }

     @Override
     void settingsEvaluated(Settings settings) {

     }

     @Override
     void projectsLoaded(Gradle gradle) {

     }

     @Override
     void projectsEvaluated(Gradle gradle) {

     }

     @Override
     void buildFinished(BuildResult buildResult) {
         println "Task spend time:DISPLAY_TIME_THRESHOLD = ${DISPLAY_TIME_THRESHOLD}"
         for (time in times) {
             if (time[0] >= DISPLAY_TIME_THRESHOLD) {
                 printf " %12sms     %s\n", time
             }
         }
     }

     @Override
     void beforeExecute(Task task) {
         clocks[task.path] = new Clock(System.currentTimeMillis())
     }

     @Override
     void afterExecute(Task task, TaskState taskState) {
         clocks.remove(task.path).with { clock ->
             def ms = clock.timeInMs
             times.add([ms,task.path])
             task.project.logger.warn "${task.path} spend ${ms}ms"
         }
     }

     static def formatTime(ms) {
         def sec = ms.intdiv(1000)
         def min = sec.intdiv(60)
         sec %= 60
         ms = (ms % 1000).intdiv(10)
         return String.format("%02d:%02d.%02d", min, sec, ms)
     }

}
