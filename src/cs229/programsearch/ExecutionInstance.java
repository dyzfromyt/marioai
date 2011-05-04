/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.programsearch;

/**
 *
 * @author Administrator
 */
public interface ExecutionInstance
{
    public Program getProgram();
    public void getExecutionEnv();

    // Execution Time
    public long getStartTime();
    public long getEndTime();
    public long getDuration();

    // Execution Result()
    public void Run();
    
}
