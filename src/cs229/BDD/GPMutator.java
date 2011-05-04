/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import cs229.programsearch.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GPMutator
{
    public static GPTree initTree(EvalEnv env, GPParams params)
    {
        GPTree result = new GPTree();
        result.root = genRandomFunctionNode(Boolean.class, env, params.initTerminalProb, params.terminalIsConstProb, params.initMaxDepth);
        
        result.seal();
        return result;
    }

    public static GPNode selectRandomNode(GPTree tree1)
    {
        List<GPNode>  allnodes = tree1.getAllNodes();
        return selectRandomNode(allnodes);
    }
    public static GPNode selectRandomNode(List<GPNode> allnodes)
    {
        Random rand = new Random();
        int selected = rand.nextInt(allnodes.size());
        return allnodes.get(selected);
    }
    public static GPNode selectRandomFunctionNode(GPTree tree1)
    {
        List<GPNode> allnodes = tree1.getAllFuncNodes();
        Random rand = new Random();
        int selected = rand.nextInt(allnodes.size());
        return allnodes.get(selected);
    }

//    public static void changeChildTo(List<GPNode> input, GPNode oldChild, GPNode newChild)
//    {
//        for(GPNode c : input)
//        {
//            if(c.changeChildTo(oldChild, newChild))
//            {
//                // change successfull
//                break;
//            }
//        }
//    }
    public static List<GPNode> removeNodeOfType(List<GPNode> input, Class type)
    {
        List<GPNode> result = new ArrayList<GPNode>();
        for (GPNode node : input)
        {
            if (!node.getType().equals(type))
            {
                result.add(node);
            }
        }
        return result;
    }
    public static List<GPNode> filterNodeOfType(List<GPNode> input, Class type)
    {
        List<GPNode> result = new ArrayList<GPNode>();
        for(GPNode node : input)
        {
            if(node.getType().equals(type))
            {
                result.add(node);
            }
        }
        return result;
    }
    
    public static List<GPTree> uniformCrossOver(GPTree tree1, GPTree tree2)
    {
        List<GPTree> result = new ArrayList<GPTree>();

        GPTree child1 = new GPTree();
        GPTree child2 = new GPTree();
        child1.root = tree1.root.makeACopy();
        child2.root = tree2.root.makeACopy();

        List<GPNode> nodeList1 = new ArrayList<GPNode>();
        child1.root.collectAllChildrenNodes(nodeList1);
        List<GPNode> nodeList2 = new ArrayList<GPNode>();
        child2.root.collectAllChildrenNodes(nodeList2);

        List<GPNode> filteredNodeList1 = nodeList1;
        GPNode node1 = selectRandomNode(filteredNodeList1);

        List<GPNode> filteredNodeList2 = filterNodeOfType(nodeList2, node1.getType());
        while(filteredNodeList2.size() == 0)
        {
            System.out.print("Rand Select Node1 is type: " + node1.getType().getName() + ", tree2 has no node of such type");
            // remove failed type and try again
            filteredNodeList1 = removeNodeOfType(filteredNodeList1, node1.getType());
            if(filteredNodeList1.size() == 0)
            {
                System.out.print("all nodes removed, failed cross over");
                return result;
            }
            node1 = selectRandomNode(filteredNodeList1);
            filteredNodeList2 = filterNodeOfType(nodeList2, node1.getType());
        }
        GPNode node2 = selectRandomNode(filteredNodeList2);
        boolean result1 = child1.changeChildTo(node1, node2);
        boolean result2 = child2.changeChildTo(node2, node1);

        
        if(result1 != true)
        {
            throw new RuntimeException("cannot change child1");
        }
        if (result2 != true)
        {
            throw new RuntimeException("cannot change child2");
        }
        //changeChildTo(nodeList1, node1, node2);
        //changeChildTo(nodeList2, node2, node1);

        child1.seal();
        child2.seal();
        result.add(child1);
        result.add(child2);
        return result;
    }

    public static void TerminalNodeMutation(TerminalNode node, EvalEnv env)
    {
        // do not change source and type
        if (node.source == TerminalNode.TerminalSource.Constant)
        {
            node.constValue = genRandomConstValue(node.getType());
        }
        else if (node.source == TerminalNode.TerminalSource.Var)
        {
            node.varID = genRandomVariableID(node.getType(), env);
        }
        else
        {
            throw new RuntimeException("unknown source: " + node.source);
        }
    }
    public static void functionNodeCondSwapMutation(FunctionNode node)
    {
        if( node.function instanceof OpCode4TIfFloatFloat
                || node.function instanceof OpCode4TIfIntInt
                || node.function instanceof OpCodeDiv
                || node.function instanceof OpCodeDivFloat
                || node.function instanceof OpCodeGreatThan
                || node.function instanceof OpCodeGreatThanFloat
                || node.function instanceof OpCodeSubstract
                || node.function instanceof OpCodeSubstractFloat
                )
        {
            // can swap first two child
            GPNode node0 = node.getChildNodeList().get(0);
            GPNode node1 = node.getChildNodeList().get(1);
            node.getChildNodeList().set(0, node1);
            node.getChildNodeList().set(1, node0);
        }
    }

    // do not change tree size
    public static GPTree pointMutationInplace(GPTree tree1, EvalEnv env, GPParams params)
    {
        GPTree child1 = new GPTree();
        child1.root = tree1.root.makeACopy();
        List<GPNode> nodeList1 = new ArrayList<GPNode>();
        child1.root.collectAllChildrenNodes(nodeList1);
        GPNode node1 = selectRandomNode(nodeList1);
        Random rand = new Random();
        if(node1 instanceof FunctionNode)
        {
            int shouldGenNewBranch = rand.nextInt(2);
            if(shouldGenNewBranch == 0)
            {
                functionNodeCondSwapMutation((FunctionNode) node1);
            }
            else
            {
                GPNode newNode = genRandomFunctionNode(node1.getType(), env, params.mutationRandomFunctionTerminalProb, params.terminalIsConstProb, params.mutationRandomFunctionMaxDepth);
                //changeChildTo(nodeList1, node1, newNode);
                child1.root.changeChildTo(node1, newNode);
            }
        }
        else if (node1 instanceof TerminalNode)
        {
            TerminalNodeMutation((TerminalNode)node1, env);
        }
        child1.seal();
        return child1;
    }

    public static GPTree pointSwapMutation(GPTree tree1)
    {
        GPTree child1 = new GPTree();
        child1.root = tree1.root.makeACopy();
        List<GPNode> nodeList1 = new ArrayList<GPNode>();
        child1.root.collectAllFunctionNodes(nodeList1);
        GPNode node1 = selectRandomNode(nodeList1);
        functionNodeCondSwapMutation((FunctionNode)node1);
        child1.seal();
        return child1;
    }
    // generate random one level tree node of spefied type
    public static GPNode genRandomFunctionNode(Class type, EvalEnv env, double terminalProb, double terminalIsConstProb, int depth)
    {
        FunctionNode result = new FunctionNode();
        List<OpCode> possibleOpCode = OpCodeRegistrar.findResultType(type.getName());
        Random rand = new Random();
        int selectedOp = rand.nextInt(possibleOpCode.size());
        result.function = possibleOpCode.get(selectedOp);
        List<Class> argTypes = result.function.getOperandType();

        List<GPNode> children = result.getChildNodeList();
        // starting from 1 because 0 is the result type
        for(int i = 1 ; i < argTypes.size(); i++)
        {
            double testTerm = rand.nextDouble();
            Class childType = argTypes.get(i);
            if(testTerm < terminalProb || depth == 0)
            {
                // should terminate
                children.add(genRandomTerminalNode(childType,env, terminalIsConstProb));
            }
            else
            {
                // add another function node
                children.add(genRandomFunctionNode(childType, env, terminalProb, terminalIsConstProb, depth-1));
            }
        }
        return result;
    }
    public static int genRandomVariableID(Class type, EvalEnv env)
    {
        List<Integer> possibleVars = env.getVariablesOfType(type);
        Random rand = new Random();
        if (possibleVars.size() == 0)
        {
            throw new RuntimeException("no such type in env: " + type.getName() + " remove unused OpCode Types and try again");
        }
        int selectedID = rand.nextInt(possibleVars.size());
        return possibleVars.get(selectedID);
    }
    public static Object genRandomConstValue(Class type)
    {
        Random rand = new Random();
        if(type.equals(Integer.class))
        {
            return rand.nextInt(5);
        }
        else if(type.equals(Boolean.class))
        {
            int trueFalse = rand.nextInt(2);
            return trueFalse == 0;
        }
        else if (type.equals(Float.class))
        {
            return rand.nextFloat();
        }
        throw new RuntimeException("unknown type: " + type.getName());
    }

    public static TerminalNode genRandomTerminalNode(Class type, EvalEnv env, double isConstProb)
    {
        TerminalNode result = new TerminalNode();
        Random rand = new Random();
        
        result.type = type;
        if(rand.nextDouble() < isConstProb)
        {
            // generate a constatnt node
            result.source = TerminalNode.TerminalSource.Constant;
            result.constValue = genRandomConstValue(type);
        }
        else
        {
            // generate a variable node
            result.source = TerminalNode.TerminalSource.Var;
            result.varID = genRandomVariableID(type, env);
        }
        return result;
    }

}
