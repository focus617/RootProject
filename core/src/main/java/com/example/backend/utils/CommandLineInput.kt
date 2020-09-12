package com.example.backend.utils

import java.util.*

enum class CommandLineInput(val shortCmd: Char) {
    FIND_ALL('a'),
    FIND_BY_ID('f'),
    INSERT('i'),
    UPDATE('u'),
    DELETE('d'),
    EXIT('e');

    companion object {
        private var INPUTS: MutableMap<Char, CommandLineInput>? = null

        init {
            INPUTS = HashMap()
            for (input in values()) {
                (INPUTS as HashMap<Char, CommandLineInput>)[input.shortCmd] = input
            }
        }


        fun getCommandLineInputForInput(input: Char): CommandLineInput? {
            return INPUTS!![input]
        }
    }

}