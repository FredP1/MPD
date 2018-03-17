package com.fred.n0568843.mpd;

import java.util.Date;

/**
 * Created by Fred on 15/03/2018.
 */

public class Note {
    String module;
    String title;
    String noteContents;
    Date noteCreated;

    public Note()
    {
    }

    public Note(String m, String t, String note, Date f)
    {
        module = m;
        title = t;
        noteContents = note;
        noteCreated = f;
    }
}

