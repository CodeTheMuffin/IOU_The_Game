package com.iou;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

import static com.iou.IOU.PIXELS_PER_METER;
public class Energy_Drink extends GAME_OBJECT{

    Sprite e_drink_spr;
    public Body e_body;

    public final float e_width_px = 32, e_height_px =32;//in pixels
    public final float e_world_width = e_width_px/PIXELS_PER_METER;//in meters
    public final float e_world_height = e_height_px/PIXELS_PER_METER;//in meters

    public Energy_Drink()
    {

    }



}
