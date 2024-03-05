package org.amogus.restarogus.exceptions

import org.springframework.security.core.AuthenticationException

class UserNotRegisteredException : AuthenticationException {
    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
    constructor(msg: String?) : super(msg)
}
