package com.example.representuapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Comment {
    public String userName;
    public String mainText;
    //public String issueID;
    public Instant createTime = Instant.now();

    public Comment(String name, String text) {
        this.userName = name;
        this.mainText = text;
    }

    /** Returns the creation time of this User. */
    public Instant getCreationTime() {
        return createTime;
    }

    /** Returns Instant time in MM/DD/YYYY HH:MM:SS timeFormat. */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String timeFormat() {
        Date myDate = Date.from(this.getCreationTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }

//    @Override
//    /** Compares Activities based on their creation time.
//     * Sorts latest first.
//     */
//    public int compareTo(Issue other) {
//        return -(this.getCreationTime().compareTo(other.getCreationTime()));
//    }
}
