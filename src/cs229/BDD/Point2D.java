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

// mario
public class Point2D implements Comparable
{
    float x;
    float y;
    int enemyKind;
    float distance;

    public float dist(float tx, float ty)
    {
        float dx = tx - x;
        float dy = ty - y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    public int compareTo(Object o)
    {
        Point2D other = (Point2D)o;
        if(distance < other.distance)
        {
            return -1;
        }
        else if (distance == other.distance)
        {
            return 0;
        }
        return 1;
    }
}
