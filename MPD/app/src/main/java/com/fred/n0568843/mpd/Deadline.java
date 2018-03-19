package com.fred.n0568843.mpd;

/**
 * Created by N0568843 on 19/03/2018.
 */

public class Deadline {
    String deadlineName;
    String module;
    String deadlineDate;
    String deadlineTime;

    public Deadline(){

    }

    public Deadline(String DeadlineName, String Module, String DeadlineDate, String DeadlineTime)
    {
        deadlineName = DeadlineName;
        module = Module;
        deadlineDate = DeadlineDate;
        deadlineTime = DeadlineTime;
    }
}
