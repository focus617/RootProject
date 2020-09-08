val sourceSets = the<SourceSetContainer>()

// creation of a source set for integration tests
sourceSets {
    create("integTest") {
        java.srcDir(file("src/integTest/kotlin"))
        resources.srcDir(file("src/integTest/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

// For the purpose of test execution
tasks.register<Test>("integTest") {
    description = "Runs the integration tests."
    group = "verification"
    testClassesDirs = sourceSets["integTest"].output.classesDirs
    classpath = sourceSets["integTest"].runtimeClasspath
    mustRunAfter(tasks["test"])
}

tasks.named("check") {
    dependsOn("integTest")
}