package com.iou;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

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

    public ArrayList< Assignments> assignments_spawned;

    public SpawnManager(int level, Batch b, World w)
    {
        batch = b;
        world = w;
        assignments_spawned= new ArrayList<Assignments>();
        spawn_timer = new Timer(spawn_timer_duration);
        spawned= 0;
        setup(level);
    }

    private void setup(int level)
    {
        current_level = level;
        assignments_spawned.clear();

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
            //ready to spawn!
            assignments_spawned.add(new Assignments(world));
            spawn_timer.resetTimer();
        }
    }




}
