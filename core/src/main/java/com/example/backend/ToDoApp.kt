package com.example.backend

import com.example.backend.utils.CommandLineInput
import com.example.backend.utils.CommandLineInputHandler

class ToDoApp {

    private val DEFAULT_INPUT = '\u0000'

    fun mainTodo(vararg args: String) {
        val commandLineInputHandler = CommandLineInputHandler()
        var command: Char = DEFAULT_INPUT

        while (CommandLineInput.EXIT.shortCmd != command) {
            commandLineInputHandler.printOptions()

            val input: String = commandLineInputHandler.readInput()
            val inputChars =
                if (input.length == 1) input.toCharArray()
                else charArrayOf(DEFAULT_INPUT)

            command = inputChars[0]
            val commandLineInput=
                CommandLineInput.getCommandLineInputForInput(command)
            commandLineInputHandler.processInput(commandLineInput)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val app = ToDoApp()
            app.mainTodo(args.toString())
        }
    }
}