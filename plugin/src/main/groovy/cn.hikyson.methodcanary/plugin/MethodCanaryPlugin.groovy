package cn.hikyson.methodcanary.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MethodCanaryPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.logger.quiet("MethodCanaryPlugin entry")
        try {
            if (!project.plugins.hasPlugin(AppPlugin)) {
                throw new GradleException('MethodCanaryPlugin: Android Application plugin [com.android.application] required.')
            }
            def android = project.extensions.android
            if (android != null) {
                android.registerTransform(new MethodCanaryTransform(project))
                project.logger.quiet("MethodCanaryPlugin registerTransform.")
            } else {
                throw new GradleException('MethodCanaryPlugin: extensions android required.')
            }
        } catch (Throwable e) {
            throw new GradleException(String.valueOf(e))
        }
    }
}