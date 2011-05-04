/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import cs229.programsearch.EvalEnv;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public interface GPNode
{
    public boolean isTerminal();
    public List<GPNode> getChildNodeList();
    public void collectAllChildrenNodes(List<GPNode> result);
    public void collectAllFunctionNodes(List<GPNode> result); // excluding terminal node
    public boolean isParentOf(GPNode child);
    public boolean changeChildTo(GPNode oldChild, GPNode newChild);
    public Object getValue(EvalEnv env);
    public Class getType();
    public GPNode makeACopy();
    public int getSize();
    public boolean isEquivalent(Object o);
    public void save(Writer writer) throws IOException;

    
}
