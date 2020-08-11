package com.example.pomodoro2.framework.extension

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

/**
 * Helper methods for Fragment class to handling navigation {with extra Info} following Nav_graph.
 * what is happening in this function:
 * 1. Make sure the current destination exists, by checking if the passed destination’s action id
 *    resolves to an action on the current destination. This avoids attempting to navigate on
 *    non-existent destinations
 * 2. Passing FragmentNavigatorExtras instance with the extra information into NavController through
 *    its navigate method.
 *
 * @sample
 *    val toDestFragmentDirections = FragmentDirections.toDestinationFragment(parameters)
 *    navigate(toDestFragmentDirections, FragmentNavigatorExtras(view to string))
 */
fun Fragment.navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
    with(findNavController()) {
        currentDestination?.getAction(destination.actionId)
            ?.let { navigate(destination, extraInfo) }
    }