package org.amogus.restarogus.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.web.context.request.WebRequest

class ProblemDetailsErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(webRequest: WebRequest, options: ErrorAttributeOptions): MutableMap<String, Any> {
        val errorAttributes = super.getErrorAttributes(webRequest, options)
        val orderedErrorAttributes = LinkedHashMap<String, Any>()
        orderedErrorAttributes["type"] = "about:blank"
        orderedErrorAttributes["title"] = errorAttributes["error"] as Any
        orderedErrorAttributes["status"] = errorAttributes["status"] as Any
        orderedErrorAttributes["detail"] = getMessage(webRequest, getError(webRequest))
        orderedErrorAttributes["instance"] = errorAttributes["path"] as Any
        return orderedErrorAttributes
    }
}