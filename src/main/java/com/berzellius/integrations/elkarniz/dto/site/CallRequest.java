package com.berzellius.integrations.elkarniz.dto.site;

import java.util.List;

/**
 * Created by berz on 15.06.2016.
 */
public class CallRequest {
    public CallRequest() {
    }

    protected List<CallDTO> calls;

    public List<CallDTO> getCalls() {
        return calls;
    }

    public void setCalls(List<CallDTO> calls) {
        this.calls = calls;
    }
}
