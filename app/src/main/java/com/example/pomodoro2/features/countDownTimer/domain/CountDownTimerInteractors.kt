package com.example.pomodoro2.features.countDownTimer.domain

/**
 * Use Case of Activity
 * 1. Adding a activity to a currently selected task.
 * 2. Removing a activity from a currently selected task.
 * 3. Getting all activities for currently selected tasks.
 * 4. Getting selected activity
 * 5. Set selected activity
 */
data class CountDownTimerInteractors(
    val tmp:Int = 0
    //TODO: Add use case set for countdown timer feature here
/*    val addActivity: AddActivity,
    val getActivities: GetActivities,
    val deleteActivity: RemoveActivity,
    val getSelectedActivity: GetSelectedActivity,
    val setSelectedActivity: SetSelectedActivity
*/
)
