package com.apro.crypto.loan

import com.apro.crypto.mvi.EventPublisher
import com.apro.crypto.mvi.Middleware

class LoanMiddleware(eventPublisher: EventPublisher) :
    Middleware<LoanAction, LoanEffect>(eventPublisher)
