/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Administrator
 */
public class GPStats implements Comparable
{
    int score;
    int numJump;

    public boolean hasLargerThan(GPStats other)
    {
        return score>other.score || numJump < other.numJump;
    }
    public int compareTo(Object o)
    {
        if(!(o instanceof GPStats))
        {
            throw new RuntimeException("Wrong type to compare");
        }
        GPStats other =(GPStats) o;
        boolean thisLargerThanThat = hasLargerThan(other);
        boolean thatLargerThanThis = other.hasLargerThan(this);
        if(thisLargerThanThat && thatLargerThanThis)
        {
            return 0;
        }
        else if(thisLargerThanThat)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public void save(Writer writer) throws IOException
    {
        writer.write("Score: " + score + " ");
        writer.write("numJump: " + numJump + "\n");
    }

    public static GPStats Load(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        GPStats result = new GPStats();
        scanner.next();
        result.score = scanner.nextInt();
        scanner.next();
        result.numJump = scanner.nextInt();
        return result;
    }


    public boolean isEquivalent(Object obj)
    {
        if (!(obj instanceof GPStats))
        {
            return false;
        }
        GPStats other = (GPStats) obj;
        if (score != other.score)
        {
            return false;
        }
        if (numJump != other.numJump)
        {
            return false;
        }
        return true;
    }

    void add(GPStats curResult)
    {
        this.numJump += curResult.numJump;
        this.score += curResult.score;
    }


}
