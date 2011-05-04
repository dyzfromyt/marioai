/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GPAgentComp extends GPAgent
{
    public GPAgentComp()
    {
        this.agentName = "GPAgent";
        PopulationPool bestPool = null;
        try
        {
            bestPool = PopulationPool.Load(new Scanner(new File("D:\\report.txt")));
        }
        catch(Exception e)
        {
            throw new RuntimeException("unable to load");
        }
        this.tree = bestPool.pool.get(1).tree;
    }
}
