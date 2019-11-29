package com.adzi.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginAdzImpl implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('testDzTask') {
            println "Hello gradle plugin"
        }
    }
}