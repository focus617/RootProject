package com.example.pomodoro2.buildSrcPlugin;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class GreetingPluginTest {
    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.example.pomodoro2.buildSrcPlugin.BuildSrcPlugin");

        assertTrue(project.getTasks().getByName("hello") instanceof GreetingTask);
    }
}