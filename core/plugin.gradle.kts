
apply(plugin = "com.focus617.standAlonePlugin")
apply(plugin = "com.focus617.BuildSrcPlugin")

// Configure the extension using a DSL block
configure<com.example.pomodoro2.buildSrcPlugin.GreetingPluginExtension> {
    message = "Hello from GreetingPlugin"
    greeter = "Gradle"
}

// Used for task "greeting" for using of extra properties
extra["Output"] = "true"

// Used for task "sayGreeting" for using of extra properties
extra["greetingFile"] = "$buildDir/hello.txt"

/**
 * demonstrates the use of extra properties
 * (equivalent of the ext properties found in Gradle Script Groovy)
 */
tasks {

    val myTask by creating {
        group = "pluginTest"

        val foo by extra { 42 }
        val bar by extra<Int?>(null)

        doLast {
            println("Extra foo property value: $foo")
            println("Optional extra bar property value: $bar")
        }
    }

    val myTest by registering {
        group = "pluginTest"
        dependsOn(myTask)

        doLast {
            val foo: Int by myTask.extra
            val bar: Int? by myTask.extra

            println("myTask.foo = $foo")
            println("myTask.bar = $bar")
        }
    }

    defaultTasks(myTest.name)
}