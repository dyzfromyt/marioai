/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cs229.common;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class Utils
{
    private static <K, V> void saveHashMap(HashMap<K, V> data, Writer outStream) throws IOException
    {
        Set<Map.Entry<K, V>> dataSet = data.entrySet();
        int setSize = dataSet.size();
        outStream.write("" + setSize + " ");
        for (Map.Entry<K, V> entry : dataSet)
        {
            outStream.write("" + entry.getKey().toString() + " ");
            outStream.write("" + entry.getValue().toString() + " ");
        }
    }
    public static Object LoadObject(Scanner scanner) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        String nodeType = scanner.next();
        Class classToLoad = Class.forName(nodeType);
        Method loadMethod = classToLoad.getDeclaredMethod("Load", Scanner.class);
        return loadMethod.invoke(null, scanner);
    }

    public static byte [] convertArray2Dto1D(byte [][] data)
    {
        int l1Length = data.length;
        int totalCount = 0;
        for(int i = 0 ; i < l1Length; i++)
        {
            totalCount += data[i].length;
        }
        byte [] result = new byte [totalCount];
        int curIndex = 0;
        for (int i = 0; i < l1Length; i++)
        {
            int l2Length = data[i].length;
            for (int j = 0; j < l1Length; j++)
            {
                result[curIndex] = data[i][j];
                curIndex ++;
            }
        }
        return result;
    }
    
    private static <K, V> HashMap<K, V> loadHashMap(Scanner scanner) throws IOException
    {
        int setSize = scanner.nextInt();

        for (int i = 0; i < setSize; i++)
        {
        }
        return null;
    }

    private static <V> void saveArrayList(ArrayList<V> data, Writer outStream)
    {
    }

}
