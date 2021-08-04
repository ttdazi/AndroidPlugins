package com.ttdazi.gradle
import com.android.build.gradle.AppExtension
import com.ttdazi.gradle.model.ModelExtension
import com.ttdazi.gradle.transform.AspectJTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginRoute implements Plugin<Project> {
    @Override
    void apply(Project project) {
        ModelExtension extension = project.extensions.create("modelName", ModelExtension)
        println("------------ apply  model extension --------------" +  extension.modelName)
        boolean hasApp = project.getPlugins().hasPlugin(AppPlugin.class)
        if (hasApp) {
            AppExtension appExtension = project.getExtensions().getByType(AppExtension.class)
            appExtension.registerTransform(new AspectJTransform(), Collections.EMPTY_LIST)
        }
    }


}