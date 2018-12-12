package com.iou;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpawnManager {
    //ArrayList<>
    private int REQ_hw= 100, REQ_projects = 100;//the number of required hw/projects player must complete
    private int MAX_hw_spawns= 100, MAX_proejcts_spawns = 100;//max amount of hw/projects spawned. Always > REQ
    int completed_hw = 0, completeted_projects= 0;

    public SpawnManager(int level)
    {
        if(level == 1)
        {
            REQ_hw = 5;REQ_projects = 2;
            MAX_hw_spawns = 8; MAX_proejcts_spawns= 4;
        }
        else if(level == 2)
        {
            REQ_hw = 8;REQ_projects = 3;
            MAX_hw_spawns = 15; MAX_proejcts_spawns= 5;
        }
        else if (level == 3)
        {
            REQ_hw = 10;REQ_projects = 4;
            MAX_hw_spawns = 15; MAX_proejcts_spawns= 6;
        }
        else if (level == 4)
        {
            REQ_hw = 12;REQ_projects = 6;
            MAX_hw_spawns = 15; MAX_proejcts_spawns= 8;
        }
    }



}
