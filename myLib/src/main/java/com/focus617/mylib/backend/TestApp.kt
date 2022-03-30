package com.focus617.mylib.backend


import CommandLineInputHandler
import com.example.backend.utils.CommandLineInput
import org.apache.commons.lang3.CharUtils

class TestApp {

    private val DEFAULT_INPUT = '\u0000'

    @JvmName("TestApp")
    fun main(vararg args: String) {
        val commandLineInputHandler = CommandLineInputHandler()
        var command: Char = DEFAULT_INPUT

        while (CommandLineInput.EXIT.shortCmd != command) {
            commandLineInputHandler.printOptions()

            val input: String = commandLineInputHandler.readInput()
            command = CharUtils.toChar(input, DEFAULT_INPUT)

            val commandLineInput=
                CommandLineInput.getCommandLineInputForInput(command)
            commandLineInputHandler.processInput(commandLineInput)
        }
    }


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = TestApp()
            app.main(args.toString())
        }
    }
}