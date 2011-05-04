/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class TransProbEntry implements Serializable
{
    public int state;
    public int obsFrequency;
    public double prob;

    public TransProbEntry()
    {
        state = 0;
        obsFrequency = 0;
        prob = 0.0;
    }
    
    @Override
    public String toString()
    {
        return "" + state + " " + obsFrequency + " " + prob;
    }

    public TransProbEntry makeACopy()
    {
        TransProbEntry result = new TransProbEntry();
        result.obsFrequency = obsFrequency;
        result.prob = prob;
        result.state = state;
        return result;
    }
    public static TransProbEntry load(Scanner scanner)
    {
        TransProbEntry result = new TransProbEntry();
        result.state = scanner.nextInt();
        result.obsFrequency = scanner.nextInt();
        result.prob = scanner.nextDouble();
        return result;
    }
}
