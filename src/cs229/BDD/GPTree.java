/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import java.util.*;
import java.io.*;
import cs229.common.*;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Administrator
 */
public class GPTree
{
    public GPNode root;

    int treeSize = -1;
    List<GPNode> allNodes = null;
    List<GPNode> allFuncNodes = null;
    boolean sealed = false;
    public void seal()
    {
        treeSize = root.getSize();
        allNodes = new ArrayList<GPNode>();
        allFuncNodes = new ArrayList<GPNode>();
        root.collectAllChildrenNodes(allNodes);
        root.collectAllFunctionNodes(allFuncNodes);
        sealed = true;
    }
    public boolean changeChildTo(GPNode oldChild, GPNode newChild)
    {
        if(root == oldChild)
        {
            root = newChild;
            return true;
        }
        else
        {
            return root.changeChildTo(oldChild, newChild);
        }
    }
    public int getSize()
    {
        if(!sealed)
        {
            throw new RuntimeException("seal it first...");
        }
        return treeSize;
    }
    public List<GPNode> getAllNodes()
    {
        if (!sealed)
        {
            throw new RuntimeException("seal it first...");
        }
        return allNodes;
    }
    public List<GPNode> getAllFuncNodes()
    {
        if (!sealed)
        {
            throw new RuntimeException("seal it first...");
        }
        return allFuncNodes;
    }

    public void save(Writer writer) throws IOException
    {
        this.root.save(writer);
        writer.write(" ");
    }

    public static GPTree Load(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        GPTree result = new GPTree();
        result.root = (GPNode) Utils.LoadObject(scanner);
        result.seal();
        return result;
    }

    public boolean isEquivalent(Object obj)
    {
        if (!(obj instanceof GPTree))
        {
            return false;
        }
        GPTree other = (GPTree) obj;
        if(!root.isEquivalent(other.root))
        {
            return false;
        }
        return true;
    }

}
