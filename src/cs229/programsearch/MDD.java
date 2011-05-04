/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.programsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class MDD
{
    private List<MDDNode> nodeList;
    private Set<Integer> varIDSet;
    private MDDNode entryPoint;
    public MDD()
    {
        nodeList = new ArrayList<MDDNode>();
        varIDSet = new HashSet<Integer>();
        //entryPoint = addValueNode(0);
    }

    public MDDNode getRandomVariableNode()
    {
        return null;
    }
    

    public MDDNode getEntryPoint()
    {
        return entryPoint;
    }


    public static void main(String [] args)
    {
        OpCodeRegistrar.printTypeToOpCodeList();
    }
    

}
