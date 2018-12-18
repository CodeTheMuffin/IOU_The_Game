package com.iou;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;


//import static com.iou.Assignments.world;
import static com.iou.GAME_OBJECT.generate_speed;
import static com.iou.GAME_OBJECT.generate_starting_Y;
import static com.iou.GameScreen.print;

/*
* This should be a generic class, but due to lack of time, we combined two 'would-have-been' objects into one.
*(Rob)
* */
public class SpawnManager {
    /*
    private int REQ_hw= 100, REQ_projects = 100;//the number of required hw/projects player must complete
    //private int MAX_hw_spawns= 100, MAX_projects_spawns = 100;//max amount of hw/projects spawned. Always > REQ
    private int completed_hw = 0, completeted_projects= 0;
    private final int HW_POINTS = 10, PROJECT_POINTS = 25;*/
    private int spawned = 0;
    private float spawn_Assignment_timer_duration , spawn_E_Drink_timer_duration;
    private Timer spawn_Assignment_timer,spawn_E_Drink_timer ;

    private int current_level = 1;

    private ArrayList<Body > BodiesToBeDeleted;

    //the difference between the max and min start Y values for Assignments
    //private final static float Y_Start_diff =  MAX_START_Y- MIN_START_Y;

    public ArrayList< Assignments> Assignments_Spawned;
    public ArrayList<Energy_Drink> Energy_Drinks_Spawned;

    public SpawnManager(int level, Batch b, World w)
    {
        //batch = b;
        //world = w;
        Assignments_Spawned= new ArrayList<Assignments>();
        Energy_Drinks_Spawned= new ArrayList<Energy_Drink>();
        spawned= 0;
        setup(level);
    }

    public void nextLevel()
    {
        setup( current_level++ );
    }

    private void setup(int level)
    {
        current_level = level;
        Assignments_Spawned.clear();
        Energy_Drinks_Spawned.clear();
        BodiesToBeDeleted = new ArrayList<Body>();

        //the levels are the years
        if(level == 1)
        {
            //REQ_hw = 5;REQ_projects = 2;
            //MAX_hw_spawns = 8; MAX_projects_spawns= 4;
            spawn_Assignment_timer_duration = 2.2f;
            spawn_E_Drink_timer_duration = 3f;
        }
        else if(level == 2)
        {
            //REQ_hw = 8;REQ_projects = 3;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 5;
            spawn_Assignment_timer_duration= 1.7f;
            spawn_E_Drink_timer_duration = 3f;
        }
        else if (level == 3)
        {
            //REQ_hw = 10;REQ_projects = 4;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 6;
            spawn_Assignment_timer_duration= 1.5f;
            spawn_E_Drink_timer_duration = 4f;
        }
        else if (level == 4)
        {
            //REQ_hw = 12;REQ_projects = 6;
            //MAX_hw_spawns = 15; MAX_projects_spawns= 8;
            spawn_Assignment_timer_duration = 0.8f;
            spawn_E_Drink_timer_duration = 4f;
        }

        spawn_Assignment_timer  = new Timer(spawn_Assignment_timer_duration);
        spawn_E_Drink_timer     = new Timer(spawn_E_Drink_timer_duration);
    }

    public int get_current_lvl(){return current_level;}

    //checks if its time to spawn
    public void checkSpawnTimer(World world)
    {
        if(spawn_Assignment_timer.isTimeUp())
        {
            float the_starting_Y = generate_starting_Y();
            float the_starting_speed = generate_speed();

            Assignments_Spawned.add(new Assignments(world, the_starting_speed, 4f, the_starting_Y));
            spawn_Assignment_timer.resetTimer();
        }

        if(spawn_E_Drink_timer.isTimeUp())
        {
            float the_starting_Y = generate_starting_Y();
            float the_starting_speed = generate_speed();

            Energy_Drinks_Spawned.add(new Energy_Drink(world, the_starting_speed, 4f, the_starting_Y));
            spawn_E_Drink_timer.resetTimer();
        }
    }

    public void pause_spawns()
    {
        for(Assignments assignments: Assignments_Spawned)
        {
            assignments.pauseTimer();
        }

        for(Energy_Drink drinks: Energy_Drinks_Spawned)
        {
            drinks.pauseTimer();
        }
    }

    //draw all the spawns
    public void draw_spawns(Batch the_batch, World the_world, boolean isPaused)
    {
        if(!isPaused)
            checkSpawnTimer(the_world);

        for(int i = 0; i< Assignments_Spawned.size(); i++)
        {
            Assignments assignment = Assignments_Spawned.get( i );

            if(!assignment.isTimerDone())
                assignment.draw_me(the_batch);
            else//if timer is done or if ready to die
            {
                //if(assignment.getBody() != null)//if there is still a body, get rid of it in the world!
                //{
                    //assignment.destroy();
                    //assignment.destroy(world);//this
                //}

                if(!assignment.player_hit_me)//if its time to die, and player did not hit me
                {
                    //simulate hitting a wall
                    assignment.wall_hit();
                }

                BodiesToBeDeleted.add( assignment.getBody() );
                Assignments_Spawned.remove( i );
                i--;

                if(Assignments_Spawned.size() ==0)
                    break;
            }
        }



        for(int i = 0; i< Energy_Drinks_Spawned.size(); i++)
        {
            Energy_Drink drinks = Energy_Drinks_Spawned.get( i );

            if(!drinks.isTimerDone())
                drinks.draw_me(the_batch);
            else//if timer is done or if ready to die
            {
                BodiesToBeDeleted.add( drinks.getBody() );
                Energy_Drinks_Spawned.remove( i );
                i--;

                if(Energy_Drinks_Spawned.size() ==0)
                    break;
            }
        }
    }

    //delete bodies that are ready to be dead
    public void DeleteBodies(World world)
    {
        for(Body body: BodiesToBeDeleted)
        {
            world.destroyBody( body );
        }
        BodiesToBeDeleted.clear();
    }


}
