package com.ttdazi.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginTime implements Plugin<Project> {
    private String TAG=getClass().getClass().getName()
    @Override
    void apply(Project project) {
        project.task('testDzTask') {
            println "${TAG}___apply"
        }
    }
//
//    @Override
//    Object invokeMethod(String s, Object o) {
//        println "${TAG}___invokeMethod"
//        return super.invokeMethod(s, o)
//    }
//
//    @Override
//    void setMetaClass(MetaClass metaClass) {
//        println "${TAG}___setMetaClass"
//        super.setMetaClass(metaClass)
//    }
//
//    @Override
//    void setProperty(String s, Object o) {
//        println "${TAG}___setProperty"
//        super.setProperty(s, o)
//    }
//
//    @Override
//    Object getProperty(String s) {
//        println "${TAG}___getProperty"
//        return super.getProperty(s)
//    }
//
//    @Override
//    MetaClass getMetaClass() {
//        println "${TAG}___getMetaClass"
//        return super.getMetaClass()
//    }
}