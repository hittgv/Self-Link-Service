package io.restall.hittgv.self;

import javaslang.collection.Map;

public class Response {

    private final Integer internalLinks;

    private final Integer externalLinks;

    public Response(Map<Boolean, Integer> result) {
        internalLinks = result.get(true).getOrElse(0);
        externalLinks = result.get(false).getOrElse(0);
    }

    public Integer getInternalLinks() {
        return internalLinks;
    }

    public Integer getExternalLinks() {
        return externalLinks;
    }
}
