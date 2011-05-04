/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.BDD;


import cs229.programsearch.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Administrator
 */
public class PopulationPool
{
    List<PopulationEntry> pool;
    int intendedSize;
    public PopulationPool()
    {
        pool = new ArrayList<PopulationEntry> ();
        
    }

    public void initPop(int intendedSize, EvalEnv env, GPParams params)
    {
        this.intendedSize = intendedSize;
        for(int i = 0 ; i < intendedSize ; i++)
        {
            PopulationEntry entry = new PopulationEntry();
            entry.tree = GPMutator.initTree(env, params); // expected height 5:
            entry.stats = new GPStats() ;
            pool.add(entry);
        }
    }

    public void sortPopulation()
    {
        Collections.sort(pool);
    }
    
    // advance
    public void nextPopulation(EvalEnv exampleEnv, GPParams params)
    {
        
        Random rand = new Random();
        int treeRank1 = biasedRandomNum(pool.size()/2, rand);
        int treeRank2 = biasedRandomNum(pool.size()/2, rand);
        List<GPTree> children = new ArrayList<GPTree> ();
        double portionCrossOver = params.crossOverPortion;
        double portionMutation = params.mutationPortion;

        int newTrees = (int)(pool.size()* portionCrossOver /2);
        int mutants = (int) (pool.size() * portionMutation);
        
        // cross over
        for(int i = 0 ; i < newTrees; i ++)
        {
            children.addAll(GPMutator.uniformCrossOver(pool.get(treeRank1).tree, pool.get(treeRank2).tree));
        }

        // mutation
        for(int i = 0; i < mutants ; i++)
        {
            int treeRank4 = rand.nextInt(pool.size());
            children.add(GPMutator.pointMutationInplace(pool.get(treeRank4).tree, exampleEnv, params));
        }

        // eleminate bad pop
        while(pool.size() > 0 && (pool.size() + children.size()> intendedSize))
        {
            //int treeRank3 = pool.size() - 1 - biasedRandomNum(pool.size(), rand); // biased toward the end
//            if(treeRank3 < (pool.size()*0.3))
//            {
//                // never eliminate first 30% of the population
//                continue;
//            }
            pool.remove(pool.size() - 1 ); //
        }
        
        // add new pop
        for(GPTree tree : children)
        {
            PopulationEntry newEntry = new PopulationEntry();
            newEntry.tree = tree;
            newEntry.stats = new GPStats();
            pool.add(newEntry);
        }
    }

    // biased toward 0
    public static int biasedRandomNum(int totalSize, Random rand)
    {
        int area = (1+totalSize)*totalSize /2;
        int selected = rand.nextInt(area);
        int result = (int)((-1+Math.sqrt(1+8*selected))/2);
        result = totalSize - result-1;
        return result;
    }

 
    public void save(Writer writer) throws IOException
    {

        writer.write("" + pool.size() + "");
        writer.write("\n");
        for(PopulationEntry e : pool)
        {
            e.save(writer);
            writer.write("\n");
        }
    }

    public static PopulationPool Load(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        PopulationPool result = new PopulationPool();
        int popSize = scanner.nextInt();
        for(int  i = 0 ; i < popSize ; i++)
        {
            result.pool.add(PopulationEntry.Load(scanner));
        }
        return result;
    }

    public static void main(String [] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        OpCodeRegistrar.populateInitalOpCodePool();
        GPParams params = GPParams.setting1();
        EvalEnv env = new EvalEnv();
        env.createVariable(Boolean.class, true);
        env.createVariable(Boolean.class, true);
        env.createVariable(Boolean.class, false);
        env.createVariable(Integer.class, 0);
        env.createVariable(Integer.class, 1);
        env.createVariable(Integer.class, 2);
        env.createVariable(Integer.class, 3);
        env.createVariable(Float.class, 0.0);
        env.createVariable(Float.class, 1.1);
        env.createVariable(Float.class, 2.2);
        env.createVariable(Float.class, 3.3);

        PopulationPool pop = new PopulationPool();
        pop.initPop(2, env, params);
        Random rand = new Random();
        for (int i = 0; i < pop.pool.size(); i++)
        {
            pop.pool.get(i).stats.score = rand.nextInt(6);
            pop.pool.get(i).stats.numJump = rand.nextInt(6);
        }

        StringWriter writer1 = new StringWriter();
        pop.pool.get(0).tree.save(writer1);
        //pop.pool.get(1).tree.save(writer1);
        writer1.flush();

        //List<GPTree> newTrees = GPMutator.uniformCrossOver(pop.pool.get(0).tree, pop.pool.get(1).tree);

        GPTree mutantTree = GPMutator.pointMutationInplace(pop.pool.get(0).tree, env, params);
        
        StringWriter writer2 = new StringWriter();
        mutantTree.save(writer2);
        //newTrees.get(1).save(writer2);
        writer2.flush();

        System.out.println(writer1.toString());
        System.out.println("---------------------------------");
        System.out.println(writer2.toString());
        System.out.println(writer2.toString().equals(writer1.toString()));
    }

    void printStats()
    {
        System.out.println("Population: " + pool.size());
        for (int i = 0; i < Math.min(pool.size(), 5); i++)
        {
            System.out.println("Score: " + pool.get(i).stats.score + " \tNumJump: " + pool.get(i).stats.numJump + " \tSize: " + pool.get(i).tree.treeSize);
        }
    }


}
