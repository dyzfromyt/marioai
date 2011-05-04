/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import cs229.programsearch.EvalEnv;
import cs229.programsearch.OpCode;
import cs229.common.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class FunctionNode implements GPNode
{
    private List<GPNode> children;
    public OpCode function;
    
    public static FunctionNode Load(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, IllegalArgumentException, NoSuchMethodException
    {
        FunctionNode result = new FunctionNode();
        String opCodeStr = scanner.next();
        result.function = (OpCode) Class.forName(opCodeStr).newInstance();
        List<Class> childrenClass = result.function.getOperandType();
        for(int i= 1 ; i < childrenClass.size() ; i++)
        {
            Class c  = childrenClass.get(i);
            result.children.add((GPNode) Utils.LoadObject(scanner));
        }
        return result;
    }

    public void save(Writer writer) throws IOException
    {
        writer.write(FunctionNode.class.getName());
        writer.write(" ");
        writer.write(function.getClass().getName());
        writer.write(" ");
        for(GPNode node: children)
        {
            node.save(writer);
            writer.write(" ");
        }
        writer.write("\n");
    }

    public boolean isEquivalent(Object obj)
    {
        if(! (obj instanceof FunctionNode))
        {
            return false;
        }
        FunctionNode other = (FunctionNode) obj;
        if(!function.isEquivalent(other.function))
        {
            return false;
        }
        int childNum = children.size();
        if(childNum != other.children.size())
        {
            return false;
        }
        
        for (int i = 0 ; i < childNum ; i ++)
        {
            GPNode node = children.get(i);
            if(!node.isEquivalent(other.children.get(i)))
            {
               return false;
            }
        }
        return true;
    }



    
    public FunctionNode makeACopy()
    {
        FunctionNode result = new FunctionNode();
        for(GPNode child : children)
        {
            result.children.add(child.makeACopy());
        }
        result.function = function;
        return result;
    }
    public boolean isParentOf(GPNode child)
    {
        for (GPNode c : children)
        {
            if(c==child)
            {
                return true;
            }
        }
        return false;
    }
    public boolean changeChildTo(GPNode oldChild, GPNode newChild)
    {
        for (int i = 0 ; i < children.size(); i++)
        {
            GPNode c  = children.get(i);
            if (c == oldChild)
            {
                children.set(i, newChild);
                return true;
            }
        }
        // is not child of this node
        for (int i = 0; i < children.size(); i++)
        {
            GPNode c = children.get(i);
            boolean result = c.changeChildTo(oldChild, newChild);
            if(result == true)
            {
                return result;
            }
        }
        return false;
    }
    public int getSize()
    {
        int size = 1;
        for (GPNode child : children)
        {
            size += child.getSize();
        }
        return size;
    }
    public void collectAllChildrenNodes(List<GPNode> result)
    {
        result.add(this);
        for (GPNode child : children)
        {
            child.collectAllChildrenNodes(result);
        }
    }
    public void collectAllFunctionNodes(List<GPNode> result)
    {
        result.add(this);
        for (GPNode child : children)
        {
            child.collectAllFunctionNodes(result);
        }
    }
    public FunctionNode()
    {
        children = new ArrayList<GPNode>();
    }

    public boolean isTerminal()
    {
        return false;
    }

    public Object getValue(EvalEnv env)
    {
        List<Object> vals = new ArrayList<Object>();
        for (GPNode child : children)
        {
            vals.add(child.getValue(env));
        }
        return function.eval(vals);
    }
    public List<GPNode> getChildNodeList()
    {
        return children;
    }

    public Class getType()
    {
        return function.getOperandType().get(0);
    }

    public String toString()
    {
        return function.getClass().getSimpleName();
    }

}
