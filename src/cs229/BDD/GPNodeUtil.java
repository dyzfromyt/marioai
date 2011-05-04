/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import java.util.*;
import java.io.*;
import java.lang.reflect.*;

/**
 *
 * @author Administrator
 */
public class GPNodeUtil
{
    public static String toDot(GPNode root)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph GPNode {\n");
        List<GPNode> nodeList1 = new ArrayList<GPNode>();
        root.collectAllChildrenNodes(nodeList1);
        HashMap<GPNode, String> nodeToName = new HashMap<GPNode, String>();
        int nodeID = 0;
        for(GPNode node : nodeList1)
        {
            String nodeIDStr = "node" + nodeID;
            nodeToName.put(node, nodeIDStr);
            sb.append(nodeIDStr);
            sb.append("[label=\"");
            sb.append(node.toString());
            sb.append("\"]");
            sb.append(";");
            sb.append("\n");
            nodeID ++;
        }

        for (GPNode node : nodeList1)
        {
            
            if(node instanceof TerminalNode)
            {
                continue;
            }
            List<GPNode> children = node.getChildNodeList();
            String nodeIDStr = nodeToName.get(node);
            for(GPNode child : children)
            {
                String childNodeIDStr = nodeToName.get(child);
                sb.append(nodeIDStr);
                sb.append(" -> ");
                sb.append(childNodeIDStr);
                sb.append(";");
                sb.append("\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

}

