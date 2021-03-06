/*
 * Copyright (C) 2015 Edward Raff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jstarcraft.ai.jsat.datatransform.kernel;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.ClassificationModelEvaluation;
import com.jstarcraft.ai.jsat.classifiers.Classifier;
import com.jstarcraft.ai.jsat.classifiers.svm.DCDs;
import com.jstarcraft.ai.jsat.datatransform.DataModelPipeline;
import com.jstarcraft.ai.jsat.datatransform.kernel.RFF_RBF;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class RFF_RBFTest {

    public RFF_RBFTest() {
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

    @Test
    public void testTrainC_ClassificationDataSet_ExecutorService() {
        System.out.println("trainC");

        DataModelPipeline instance = new DataModelPipeline((Classifier) new DCDs(), new RFF_RBF(0.5, 100, true));

        ClassificationDataSet train = FixedProblems.getInnerOuterCircle(200, RandomUtil.getRandom());
        ClassificationDataSet test = FixedProblems.getInnerOuterCircle(100, RandomUtil.getRandom());

        ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train, true);
        cme.evaluateTestSet(test);

        assertEquals(0, cme.getErrorRate(), 0.0);

    }

    @Test
    public void testTrainC_ClassificationDataSet() {
        System.out.println("trainC");

        DataModelPipeline instance = new DataModelPipeline((Classifier) new DCDs(), new RFF_RBF(0.5, 100, true));

        ClassificationDataSet train = FixedProblems.getInnerOuterCircle(200, RandomUtil.getRandom());
        ClassificationDataSet test = FixedProblems.getInnerOuterCircle(100, RandomUtil.getRandom());

        ClassificationModelEvaluation cme = new ClassificationModelEvaluation(instance, train);
        cme.evaluateTestSet(test);

        assertEquals(0, cme.getErrorRate(), 0.0);

    }

    @Test
    public void testClone() {
        System.out.println("clone");

        DataModelPipeline instance = new DataModelPipeline((Classifier) new DCDs(), new RFF_RBF(0.5, 100, false));

        ClassificationDataSet t1 = FixedProblems.getInnerOuterCircle(500, RandomUtil.getRandom());
        ClassificationDataSet t2 = FixedProblems.getInnerOuterCircle(500, RandomUtil.getRandom(), 2.0, 10.0);

        instance = instance.clone();

        instance.train(t1);

        DataModelPipeline result = instance.clone();

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), result.classify(t1.getDataPoint(i)).mostLikely());
        result.train(t2);

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), instance.classify(t1.getDataPoint(i)).mostLikely());

        for (int i = 0; i < t2.size(); i++)
            assertEquals(t2.getDataPointCategory(i), result.classify(t2.getDataPoint(i)).mostLikely());

    }

}
