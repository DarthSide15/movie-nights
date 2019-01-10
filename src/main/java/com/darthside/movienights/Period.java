package com.darthside.movienights;

import com.google.api.client.util.DateTime;

public class Period {

    private DateTime start;
    private DateTime end;

    public Period(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }
}
