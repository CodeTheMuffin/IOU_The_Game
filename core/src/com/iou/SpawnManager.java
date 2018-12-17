package com.iou;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;


import static com.iou.GAME_OBJECT.generate_speed;
import static com.iou.GAME_OBJECT.generate_starting_Y;


public class SpawnManager {
    private int REQ_hw= 100, REQ_projects = 100;//the number of required hw/projects player must complete
    //private int MAX_hw_spawns= 100, MAX_projects_spawns = 100;//max amount of hw/projects spawned. Always > REQ
    private int completed_hw = 0, completeted_projects= 0;
    private final int HW_POINTS = 10, PROJECT_POINTS = 25;
    private int spawned = 0;
    private float spawn_timer_duration = 1.5f;//1.5 seconds to spawn the next
    private Timer spawn_timer;
    private Batch batch;
    private World world;
    private int current_level = 1;

    //the difference between the max and min start Y values for Assignments
    //private final static float Y_Start_diff =  MAX_START_Y- MIN_START_Y;

    public ArrayList< Assignments> Assignments_Spawned;

    public SpawnManager(int level, Batch b, World w)
    {
        batch = b;
        world = w;
        Assignments_Spawned= new ArrayList<Assignments>();
        spawn_timer = new Timer(spawn_timer_duration);
        spawned= 0;
        setup(level);
    }

    private void setup(int level)
    {
        current_level = level;
        Assignments_Spawned.clear();

        //the levels are the years
        if(level == 1)
        {
            REQ_hw = 5;REQ_projects = 2;
            //MAX_hw_spawns = 8; MAX_projects_spawns= 4;
            spawn_timer_duration = 1.0f;
        }
        else if(level == 2)
        {
            REQ_hw = 8;REQ_projects = 3;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 5;
            spawn_timer_duration= 0.8f;
        }
        else if (level == 3)
        {
            REQ_hw = 10;REQ_projects = 4;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 6;
            spawn_timer_duration= 0.6f;
        }
        else if (level == 4)
        {
            REQ_hw = 12;REQ_projects = 6;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 8;
            spawn_timer_duration = 0.3f;
        }
    }

    public int get_current_lvl(){return current_level;}






    //checks if its time to spawn
    public void manage()
    {
        if(spawn_timer.isTimeUp())
        {
            float the_starting_Y = generate_starting_Y();

            //Assignments_Spawned.add(new Assignments(world));
            Assignments_Spawned.add(new Assignments(world, -5f, 5f, the_starting_Y));
            spawn_timer.resetTimer();
        }
    }

    public void draw_spawns(Batch the_batch, World the_world)
    {
        for(int i = 0; i< Assignments_Spawned.size(); i++)
        {
            Assignments assignment = Assignments_Spawned.get( i );

            if(!assignment.isTimerDone())
                assignment.draw_me(the_batch);
            else//if timer is done or if ready to die
            {
                if(assignment.getBody() != null)//if there is still a body, get rid of it in the world!
                {
                    //assignment.destroy();
                    assignment.destroy(world);
                }

                Assignments_Spawned.remove( i );
                i--;
            }
        }

    }


}
