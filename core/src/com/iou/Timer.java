package com.iou;
import com.badlogic.gdx.utils.TimeUtils;

import static com.iou.IOU.print;

public class Timer {
    private long StartTime = TimeUtils.nanoTime();
    private long duration = 2; //in nanoseconds
    private final long SECOND_TO_NANO = 1000000000;//1e9
    private long paused_time = 0;

    //d is represented in seconds
    public Timer(float seconds)
    {
        duration = (long)(seconds*SECOND_TO_NANO);
        //print("seconds: "+ seconds + "\nduration: "+duration+"\n");
    }

    public Timer(long nanoseconds)
    {
        duration =  nanoseconds;
    }

    //is it time to set off the timer?
    public boolean isTimeUp()
    {
        return (TimeUtils.nanoTime() - StartTime  > duration);
    }

    //Not really pausing, but it's preventing object being removed after resume
    public void pauseTimer()
    {
        if(paused_time == 0)
        {
            paused_time=  TimeUtils.nanoTime() - StartTime;
            duration = paused_time;
        }
        StartTime = TimeUtils.nanoTime();
    }

    public float getTimeLeft()
    {
        long diff = TimeUtils.nanoTime() - StartTime;

        if(diff > duration)
        {
            return 0f;
        }

        return (float)(duration-diff)/SECOND_TO_NANO;
    }

    //reset the timer
    public void resetTimer()
    {
        StartTime = TimeUtils.nanoTime();
    }
}
