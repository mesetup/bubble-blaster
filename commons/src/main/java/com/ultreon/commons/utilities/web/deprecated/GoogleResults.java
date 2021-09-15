package com.ultreon.commons.utilities.web.deprecated;

import java.io.Serializable;
import java.util.List;

/**
 * @deprecated Use {@link com.ultreon.commons.utilities.web.GoogleResults} instead.
 */
@Deprecated(since = "1.0-alpha1", forRemoval = true)
public class GoogleResults implements Serializable {
    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public String toString() {
        return "ResponseData[" + responseData + "]";
    }

    static class ResponseData implements Serializable {
        private List<Result> results;

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public String toString() {
            return "Results[" + results + "]";
        }
    }

    static class Result implements Serializable {
        private String url;
        private String title;

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String toString() {
            return "Result[url=" + url + ",title=" + title + "]";
        }
    }

}