/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import cs229.programsearch.EvalEnv;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TerminalNode implements GPNode
{
    public int getSize()
    {
        return 1;
    }
    public void collectAllChildrenNodes(List<GPNode> result)
    {
        result.add(this);
    }
    public boolean changeChildTo(GPNode oldChild, GPNode newChild)
    {
        return false;
    }
    public boolean isParentOf(GPNode child)
    {
        return false;
    }
    public void collectAllFunctionNodes(List<GPNode> result)
    {
        
    }

    
    public enum TerminalSource
    {
        Constant, Var
    };
    public int varID;
    public Object constValue;
    public Class type;
    public TerminalSource source;

    public boolean isEquivalent(Object obj)
    {
        if (!(obj instanceof TerminalNode))
        {
            return false;
        }
        TerminalNode other = (TerminalNode) obj;
        if(varID != other.varID)
        {
            return false;
        }
        if (!type.equals(other.type))
        {
            return false;
        }
        if (!constValue.equals(other.constValue))
        {
            return false;
        }
        if (source != other.source)
        {
            return false;
        }

        return true;
    }
    public void save(Writer writer) throws IOException
    {
        writer.write(TerminalNode.class.getName());
        writer.write("\n");
        writer.write(source.toString());
        writer.write(" ");
        writer.write("" + varID);
        writer.write(" ");
        writer.write(type.getName());
        writer.write(" ");
        if(constValue == null)
        {
            Object obj;
            if (type == Boolean.class)
            {
                constValue = Boolean.TRUE;
            }
            else if (type == Integer.class)
            {
                constValue = Integer.MIN_VALUE;
            }
            else if (type == Float.class)
            {
                constValue = Float.NaN;
            }
            else
            {
                throw new RuntimeException("unkown type: " + type.getName());
            }
            obj = constValue;
            writer.write("" + obj.toString());
        }
        else
        {
            writer.write("" + constValue.toString());
        }
        
        writer.write("\n");
    }
    public static TerminalNode Load(Scanner scanner) throws ClassNotFoundException
    {
        TerminalNode result = new TerminalNode();
        String sourceStr = scanner.next();
        result.source = TerminalSource.valueOf(sourceStr);
        result.varID = scanner.nextInt();
        result.type = Class.forName(scanner.next());

        if(result.type == Boolean.class)
        {
            result.constValue = Boolean.parseBoolean(scanner.next());
        }
        else if (result.type == Integer.class)
        {
            result.constValue = Integer.parseInt(scanner.next());
        }
        else if (result.type == Float.class)
        {
            result.constValue = Float.parseFloat(scanner.next());
        }
        else
        {
            throw new RuntimeException("unkown type: " + result.type.getName());
        }

        return result;
    }
    public GPNode makeACopy()
    {
        TerminalNode result = new TerminalNode();
        result.varID = varID;
        result.constValue = constValue;
        result.type = type;
        result.source = source;

        return result;
    }
    
    public boolean isTerminal()
    {
        return true;
    }

    public List<GPNode> getChildNodeList()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getValue(EvalEnv env)
    {
        if(source == TerminalSource.Constant)
        {
            return constValue;
        }
        else if (source == TerminalSource.Var)
        {
            Object val = env.getVariableValue(varID);
            if(!type.isInstance(val))
            {
                throw new RuntimeException("wrong type at source: varID" + varID + " expecting type: " + type.getName() + " gotType: " + val.getClass().getName());
            }
            return val;
        }
        throw new RuntimeException("unknown terminal source");
    }

    public Class getType()
    {
        return type;
    }

    public String toString()
    {
        if (source == TerminalSource.Constant)
        {
            return constValue.toString();
        }
        else if (source == TerminalSource.Var)
        {
            return "Var: " + varID;
        }
        throw new RuntimeException("unknown terminal source");
    }
}
