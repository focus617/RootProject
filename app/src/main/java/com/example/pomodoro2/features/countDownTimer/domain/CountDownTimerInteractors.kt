package com.example.pomodoro2.features.countDownTimer.domain

/**
 * Use Case of CountdownTimer
 *
 * External Initiator:
 * 0.1 (Customer) click the right icon of selected subTask in task fragment
 * 0.2 (System) Launch countDownTimer fragment
 *
 * 1. (System) Launch Timer with initial second-tick-counting from selected subTask.
 * 2. Timer notify fragment every second to update the countdown tick number.
 * 3. (Customer) Pause the Timer
 *  3.1  (Customer) Resume the Timer
 *  3.2 Timer continue
 * 4.  (Customer) Reset the Timer
 *  4.1 Timer restart with original tick-counting
 * 5. (System) Show dialog when Timer time-up, and play music at the same time to alert Customer
 * 6. (Customer) Close the dialog, thus (System) stop the music
 * 7. (System) Go back to task fragment, and don't forgot to call timer.cancel()
 */
data class CountDownTimerInteractors(
    val tmp:Int = 0
    //TODO: Add use case set for countdown timer feature here
/*
    val launchTimerUseCase
    val pauseTimerUseCase
    val resumeTimerUseCase
    val resetTimerUseCase
    val closeTimerUseCase
 */


)
