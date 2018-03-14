package com.taskail.mixion.steemJ

/**
 *Created by ed on 3/14/18.
 *
 * All classes that user the [RxSteemJManager] object should inherit this class
 */

interface SteemJComponent {

    fun startComponent()

    fun destroyComponent()
}