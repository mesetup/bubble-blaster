package qtech.utilities.web.deprecated

import java.io.Serializable

@Deprecated("Use {@link qtech.utilities.web.GoogleResults} instead.")
class GoogleResults : Serializable {
    var responseData: ResponseData? = null
    override fun toString(): String {
        return "ResponseData[$responseData]"
    }

    class ResponseData : Serializable {
        var results: List<Result>? = null
        override fun toString(): String {
            return "Results[$results]"
        }
    }

    class Result : Serializable {
        var url: String? = null
        var title: String? = null
        override fun toString(): String {
            return "Result[url=$url,title=$title]"
        }
    }
}