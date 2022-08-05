package com.example.kafiesta.network

/**
 * Usage
Network.request(
call = call,
success = { },
fail = { }
)

 */

/**
 * Usage
 * Network.request(
 *          doOnSubscribe = { loading },
 *          doOnTerminate = { init },
 *          call = call,
 *          success = { },
 *          fail = { }
 * )
 */

object Network {
    fun <T> request(
        call: Deferred<T>,
        success: ((response: T)-> Unit)?,
        error: ((throwable: Throwable)-> Unit)?= null,
        doOnSubscribe: (()-> Unit)?= null,
        doOnTerminate: (()-> Unit)?= null) {
        launch(UI) {
            doOnSubscribe?.invoke()
            try {
                success?.invoke(call.await())
            } catch (t: Throwable) {
                error?.invoke(t)
            } finally {
                doOnTerminate?.invoke()
            }
        }
    }
}