package com.iou;

import java.util.Random;

import static com.iou.IOU.print;

public class GAME_OBJECT {

    public final static float MAX_START_Y = IOU.HEIGHT*0.6f;//in pixels
    public final static float MIN_START_Y = IOU.HEIGHT*0.125f;//in pixels
    public final static float DIFF_START_Y = MAX_START_Y - MIN_START_Y;//in pixels

    public final static float MAX_START_SPEED = IOU.HEIGHT*0.6f;//in meters
    public final static float MIN_START_SPEED = IOU.HEIGHT*0.125f;//in meters
    public final static float DIFF_START_SPEED = MAX_START_SPEED - MIN_START_SPEED;//in meters


    public static float generate_starting_Y()
    {
        //get random float (0f-1f)* the difference between max and min and add that to the min y value
        return ((new Random().nextFloat()*DIFF_START_Y) + MIN_START_Y);

        //return ((1f+ new Random().nextFloat()) * MIN_START_Y);
    }

    public static float generate_speed()
    {
        //get random float (0f-1f)* the difference between max and min and add that to the min y value
        return ((new Random().nextFloat()*DIFF_START_Y) + MIN_START_Y);
        //return ( -1f*(1f+ new Random().nextFloat()) * MIN_START_Y);
    }

}
