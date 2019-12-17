package com.ttdazi.gradle

import com.ttdazi.gradle.timer.TimerBuildListener
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginTime implements Plugin<Project> {
    @Override
    void apply(Project project) {
//        project.extensions.create('extApply', Extension)
//        project.task('testDzTask').doLast() {
//            println "${TAG}___apply"
//            println 'apply_e1 = ' + project['extApply'].testVariable1
//        }
        project.getGradle().addListener(new TimerBuildListener())
    }


}