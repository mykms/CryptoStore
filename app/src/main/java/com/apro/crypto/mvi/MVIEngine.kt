package com.apro.crypto.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface MVIEngine<State, Action, Effect> : EventsSource {
    val state: StateFlow<State>
    fun CoroutineScope.startEngine()
    fun stopEngine()
    fun submitAction(action: Action)
    operator fun invoke(action: Action) = submitAction(action)
}

class MVIEngineImpl<State, Action : Any, Effect>(
    initialState: State,
    private val initialAction: Action? = null,
    private val reducer: Reducer<State, Action, Effect>,
    private val middleware: Middleware<Action, Effect>,
    eventPublisher: EventsSource,
) : MVIEngine<State, Action, Effect>, EventsSource by eventPublisher {

    private val jobByAction = mutableMapOf<Class<*>, Job>()
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)
    private val actions = MutableSharedFlow<Action>(replay = 1)

    override val state: StateFlow<State> get() = _state

    override fun CoroutineScope.startEngine() {
        actions.run {
            if (initialAction != null)
                onStart { emit(initialAction) }
            else
                this
        }.onEach {
            reduce(this, it).collect(_state::emit)
        }.launchIn(this)
    }

    override fun stopEngine() {
        jobByAction.values.filter { it.isActive }.forEach { it.cancel() }
        jobByAction.clear()
    }

    override fun submitAction(action: Action) {
        check(actions.tryEmit(action)) { "$action submitting was unsuccessful" }
    }

    private suspend fun reduce(scope: CoroutineScope, action: Action): Flow<State> = flow {
        emit(reducer.reduceAction(_state.value, action))
        if (action !is NonCancelableAction) jobByAction.remove(action.javaClass)?.cancel()
        val job = scope.launch {
            middleware.performAction(action).collect {
                _state.emit(reducer.reduceEffect(_state.value, it))
            }
        }
        if (action !is NonCancelableAction) {
            jobByAction[action.javaClass] = job
        }
    }

}
