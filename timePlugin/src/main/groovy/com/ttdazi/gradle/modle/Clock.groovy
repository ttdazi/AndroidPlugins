package com.ttdazi.gradle.modle;

/**
 * author: Y_Qing
 * date: 2019/12/16
 */
 class Clock {
     long startTimeInMs
     Clock() {
         startTimeInMs = System.currentTimeMillis();
     }
     Clock(long time) {
         this.startTimeInMs = time
     }
     long getTimeInMs() {
         return System.currentTimeMillis() - startTimeInMs
     }
 }
