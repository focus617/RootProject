package com.example.pomodoro2.features.countDownTimer.domain

/**
 * Use Case of CountdownTimer
 * 0.1 (Customer) click the right icon of selected subTask
 * 0.2 (System) Launch countDownTimer fragment
 *
 * 1. (System) Launch Timer with tick-counting from selected subTask.
 * 2. (Customer) Pause the Timer
 *  2.1  (Customer) Resume the Timer
 *  2.2 Timer continue
 * 3.  (Customer) Reset the Timer
 *  3.1 Timer restart with original tick-counting)
 * 4. (System) Show dialog when Timer time-up, and play music at the same time to alert Customer
 * 5. (Customer) Close the dialog
 * 6. (System) Go back to task fragment
 */
data class CountDownTimerInteractors(
    val tmp:Int = 0
    //TODO: Add use case set for countdown timer feature here
/*
    val launchTimerUseCase
    val pauseTimerUseCase
    val resumeTimerUseCase
    val resetTimerUseCase
    val TimeupCallback??
 */


)
