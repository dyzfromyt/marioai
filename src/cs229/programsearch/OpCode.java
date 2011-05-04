/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;


import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public abstract class OpCode
{
    public abstract List<Class> getOperandType();
    public abstract Object eval(List<Object> values);
    public abstract String getOperandTypeStr();
    public boolean isEquivalent(Object obj)
    {
        // test same class
        if (!(this.getClass().isInstance(obj)))
        {
            return false;
        }
        OpCode other = (OpCode) obj;

        return true;
    }
}
