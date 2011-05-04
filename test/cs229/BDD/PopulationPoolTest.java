/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cs229.BDD;

import cs229.programsearch.EvalEnv;
import cs229.programsearch.OpCodeRegistrar;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class PopulationPoolTest
{

    EvalEnv env = null;
    GPParams params = null;

    public PopulationPoolTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
        GPParams params = GPParams.setting1();
        env = new EvalEnv();
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
        OpCodeRegistrar.populateInitalOpCodePool();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of initPop method, of class PopulationPool.
     */
    @Test
    public void testInitPop()
    {
        System.out.println("initPop");
        int size = 10;
        PopulationPool instance = new PopulationPool();
        instance.initPop(size, env, params);
        assertTrue(instance.pool.size() == size);
    }

    /**
     * Test of rank method, of class PopulationPool.
     */
    @Test
    public void testRank()
    {
//        System.out.println("rank");
//        List<PopulationEntry> oldList = null;
//        PopulationPool instance = new PopulationPool();
//        List expResult = null;
//        List result = instance.rank(oldList);
//        assertEquals(expResult, result);
//
    }

    /**
     * Test of breed method, of class PopulationPool.
     */
    @Test
    public void testBreed()
    {
//        System.out.println("breed");
//        PopulationPool instance = new PopulationPool();
//        instance.breed();
    }

    /**
     * Test of eliminate method, of class PopulationPool.
     */
    @Test
    public void testEliminate()
    {
//        System.out.println("eliminate");
//        PopulationPool instance = new PopulationPool();
//        instance.eliminate();

    }

    /**
     * Test of testSaveLoad method, of class PopulationPool.
     */
    @Test
    public void testSaveLoad() throws Exception
    {
        System.out.println("testSaveLoad");
        int size = 10;
        PopulationPool  pop= new PopulationPool();
        pop.initPop(size, env, params);
        StringWriter writer = new StringWriter();

        pop.save(writer);
        writer.flush();
        String toLoad = writer.toString();

        Scanner scanner = new Scanner(toLoad);
        PopulationPool loadedPool = PopulationPool.Load(scanner);

        assertTrue("Same pop size", pop.pool.size() == loadedPool.pool.size());
        for (int i = 0; i < loadedPool.pool.size(); i++)
        {
            assertTrue("Pop " + i, pop.pool.get(i).isEquivalent(loadedPool.pool.get(i)));
        }
    }

}
