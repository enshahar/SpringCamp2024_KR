package com.enshahar.SpringCampR2dbcExample.listener

import org.springframework.context.ApplicationEvent
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

class TxEventListener {
    @TransactionalEventListener(phase= TransactionPhase.BEFORE_COMMIT)
    fun handleTxBeforeCommit(event: ApplicationEvent) {
        println("before commit: ${event.source} / ${event.timestamp}")
    }
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    fun handleTxAfterCommit(event: ApplicationEvent) {
        println("after commit: ${event.source} / ${event.timestamp}")
    }
    @TransactionalEventListener(phase= TransactionPhase.AFTER_ROLLBACK)
    fun handleTxAfterRollback(event: ApplicationEvent) {
        println("after rollback: ${event.source} / ${event.timestamp}")
    }
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMPLETION)
    fun handleTxAfterCompletion(event: ApplicationEvent) {
        println("after completion: ${event.source} / ${event.timestamp}")
    }
}