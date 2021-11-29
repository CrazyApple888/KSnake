interface MulticastObservable {

    fun subscribe(subscriber: MulticastObserver)

    fun unsubscribe(subscriber: MulticastObserver)

}