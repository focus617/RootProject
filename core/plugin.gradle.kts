
apply(plugin = "com.focus617.standAlonePlugin")
apply(plugin = "com.focus617.BuildSrcPlugin")

// Configure the extension using a DSL block
configure<com.example.pomodoro2.buildSrcPlugin.GreetingPluginExtension> {
    message = "Hello from GreetingPlugin"
    greeter = "Gradle"
}

extra["greetingFile"] = "$buildDir/hello.txt"