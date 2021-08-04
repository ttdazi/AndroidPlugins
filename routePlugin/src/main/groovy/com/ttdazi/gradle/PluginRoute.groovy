package com.ttdazi.gradle

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.ttdazi.gradle.model.ModelExtension
import com.ttdazi.gradle.transform.RouterTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginRoute implements Plugin<Project> {
    @Override
    void apply(Project project) {
        ModelExtension extension = project.extensions.create("modelName", ModelExtension)
        boolean hasApp = project.getPlugins().hasPlugin(AppPlugin.class)
        println("$hasApp------------ apply  model extension --------------" + extension.modelName)
        if (hasApp) {
            AppExtension appExtension = project.getExtensions().getByType(AppExtension.class)
            appExtension.registerTransform(new RouterTransform(project), Collections.EMPTY_LIST)
            //注册日志统计transform，统计的transform。美团的热更新Roubust也是在这自定义的transform。
        }
    }


}