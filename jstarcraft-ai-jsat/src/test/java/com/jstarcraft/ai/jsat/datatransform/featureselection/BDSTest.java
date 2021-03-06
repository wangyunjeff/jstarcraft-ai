
package com.jstarcraft.ai.jsat.datatransform.featureselection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.Classifier;
import com.jstarcraft.ai.jsat.classifiers.knn.NearestNeighbour;
import com.jstarcraft.ai.jsat.regression.MultipleLinearRegression;
import com.jstarcraft.ai.jsat.regression.RegressionDataSet;
import com.jstarcraft.ai.jsat.utils.random.XORWOW;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 *
 * @author Edward Raff
 */
public class BDSTest {

    public BDSTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transform method, of class BDS.
     */
    @Test
    public void testTransformC() {
        System.out.println("transformC");
        Random rand = new XORWOW(13);
        int t0 = 1, t1 = 5, t2 = 8;
        IntOpenHashSet shouldHave = new IntOpenHashSet();
        shouldHave.addAll(Arrays.asList(t0, t1, t2));

        ClassificationDataSet cds = SFSTest.generate3DimIn10(rand, t0, t1, t2);

        BDS bds = new BDS(3, (Classifier) new NearestNeighbour(7), 5).clone();
        bds.fit(cds);
        Set<Integer> found = bds.getSelectedNumerical();

        assertEquals(shouldHave.size(), found.size());
        assertTrue(shouldHave.containsAll(found));
        cds.applyTransform(bds);
        assertEquals(shouldHave.size(), cds.getNumFeatures());
    }

    @Test
    public void testTransformR() {
        System.out.println("transformR");
        Random rand = new XORWOW(13);
        int t0 = 1, t1 = 5, t2 = 8;
        IntOpenHashSet shouldHave = new IntOpenHashSet();
        shouldHave.addAll(Arrays.asList(t0, t1, t2));

        RegressionDataSet rds = SFSTest.generate3DimIn10R(rand, t0, t1, t2);

        BDS bds = new BDS(3, new MultipleLinearRegression(), 5).clone().clone();
        bds.fit(rds);
        Set<Integer> found = bds.getSelectedNumerical();

        assertEquals(shouldHave.size(), found.size());
        assertTrue(shouldHave.containsAll(found));
        rds.applyTransform(bds);
        assertEquals(shouldHave.size(), rds.getNumFeatures());
    }

}
