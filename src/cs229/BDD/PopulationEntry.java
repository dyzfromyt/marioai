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
public class PopulationEntry implements Comparable
{
    GPTree tree;
    GPStats stats;
    public void save(Writer writer) throws IOException
    {
        stats.save(writer);
        writer.write("\n");
        tree.save(writer);
        writer.write("\n");
    }

    public static PopulationEntry Load(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        PopulationEntry result = new PopulationEntry();
        result.stats = GPStats.Load(scanner);
        result.tree = GPTree.Load(scanner);
        return result;
    }
    public boolean isEquivalent(Object obj)
    {
        if (!(obj instanceof PopulationEntry))
        {
            return false;
        }
        PopulationEntry other = (PopulationEntry) obj;
        if (!stats.isEquivalent(other.stats))
        {
            return false;
        }
        if (!tree.isEquivalent(other.tree))
        {
            return false;
        }
        return true;
    }

    // better individual first
    public int compareTo(Object o)
    {
        PopulationEntry entry = (PopulationEntry)o;
        return -this.stats.compareTo(entry.stats);
    }
}
