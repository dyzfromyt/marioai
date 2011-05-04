/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

/**
 *
 * @author Administrator
 */
public interface ActionMapping
{
    public int map(boolean[] keys);
    public String getName();
    public int actionToID(boolean [] keys);
    public boolean[] idToAction(int id);
    public int getNumberOfID();
}
