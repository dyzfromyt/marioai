/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import java.util.*;
import java.io.*;

/**
 *
 * @author Administrator
 */
public class GPParams
{
    public double initTerminalProb;
    public double terminalIsConstProb;
    public double crossOverPortion;
    public double mutationPortion;
    public double mutationRandomFunctionTerminalProb; // mutation, randomly generated functional node's terminal probability
    public int maxGeneration ;
    public int initMaxDepth;
    public int mutationRandomFunctionMaxDepth;
    public String paramName = "";
    public static GPParams setting1()
    {
        GPParams result = new GPParams();
        result.initTerminalProb = 0.6; //  depth 5
        result.initMaxDepth = 5; //  depth 5
        result.terminalIsConstProb = 0.5; // half of newly generated terminal node will be constant
        result.crossOverPortion = 0.5;
        result.mutationPortion =0.25; // therefore, reproduction (simply coping) is 1-0.5-0.25 = 0.25
        result.mutationRandomFunctionTerminalProb = 0.8; // depth 3
        result.mutationRandomFunctionMaxDepth = 3;
        result.maxGeneration = 16;
        result.paramName = "setting1";
        return result;
    }

    public static GPParams setting2()
    {
        GPParams result = new GPParams();
        result.initTerminalProb = 0.4; //  depth 5
        result.initMaxDepth = 7;
        result.terminalIsConstProb = 0.3; // half of newly generated terminal node will be constant
        result.crossOverPortion = 0.4;
        result.mutationPortion = 0.4; // therefore, reproduction (simply coping) is 1-0.5-0.25 = 0.25
        result.mutationRandomFunctionTerminalProb = 0.6; // depth 3
        result.mutationRandomFunctionMaxDepth = 3;
        result.maxGeneration = 16;
        result.paramName = "setting2";
        return result;
    }

    public static GPParams setting3()
    {
        GPParams result = new GPParams();
        result.initTerminalProb = 0.4; //  depth 5
        result.initMaxDepth = 9;
        result.terminalIsConstProb = 0.3; // half of newly generated terminal node will be constant
        result.crossOverPortion = 0.8;
        result.mutationPortion = 0.05; // therefore, reproduction (simply coping) is 1-0.5-0.25 = 0.25
        result.mutationRandomFunctionTerminalProb = 0.6; // depth 3
        result.mutationRandomFunctionMaxDepth = 3; 
        result.maxGeneration = 16;
        result.paramName = "setting3";
        return result;
    }
}
