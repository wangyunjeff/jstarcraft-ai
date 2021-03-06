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
package com.jstarcraft.ai.jsat.classifiers.calibration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jstarcraft.ai.jsat.FixedProblems;
import com.jstarcraft.ai.jsat.classifiers.CategoricalData;
import com.jstarcraft.ai.jsat.classifiers.ClassificationDataSet;
import com.jstarcraft.ai.jsat.classifiers.DataPoint;
import com.jstarcraft.ai.jsat.classifiers.calibration.BinaryCalibration;
import com.jstarcraft.ai.jsat.classifiers.calibration.PlattCalibration;
import com.jstarcraft.ai.jsat.classifiers.svm.DCDs;
import com.jstarcraft.ai.jsat.datatransform.LinearTransform;
import com.jstarcraft.ai.jsat.linear.DenseVector;
import com.jstarcraft.ai.jsat.linear.Vec;
import com.jstarcraft.ai.jsat.utils.random.RandomUtil;

/**
 *
 * @author Edward Raff
 */
public class PlattCalibrationTest {

    public PlattCalibrationTest() {
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
    public void testTrain() {
        System.out.println("calibrate");

        ClassificationDataSet cds = new ClassificationDataSet(1, new CategoricalData[0], new CategoricalData(2));

        for (double pos = 0; pos < 2; pos += 0.01)
            cds.addDataPoint(DenseVector.toDenseVec(pos), 0);
        for (double pos = 1; pos < 3; pos += 0.01)
            cds.addDataPoint(DenseVector.toDenseVec(pos), 1);

        for (BinaryCalibration.CalibrationMode mode : BinaryCalibration.CalibrationMode.values()) {
            PlattCalibration pc = new PlattCalibration(new DCDs(), mode);
            pc.train(cds);

            for (int i = 0; i < cds.size(); i++) {
                DataPoint dp = cds.getDataPoint(i);
                Vec v = dp.getNumericalValues();

                if (v.get(0) < 0.25)
                    assertEquals(1.0, pc.classify(dp).getProb(0), 0.2);
                else if (1.3 < v.get(0) && v.get(0) < 1.7)
                    assertEquals(0.5, pc.classify(dp).getProb(0), 0.35);
                else if (2.75 < v.get(0))
                    assertEquals(0.0, pc.classify(dp).getProb(0), 0.2);
            }
        }
    }

    @Test
    public void testClone() {
        System.out.println("clone");
        ClassificationDataSet t1 = FixedProblems.getSimpleKClassLinear(1000, 2, RandomUtil.getRandom());
        ClassificationDataSet t2 = FixedProblems.getSimpleKClassLinear(1000, 2, RandomUtil.getRandom());

        t2.applyTransform(new LinearTransform(t2, 100, 105));

        PlattCalibration instance = new PlattCalibration(new DCDs(), BinaryCalibration.CalibrationMode.NAIVE);

        instance = instance.clone();

        instance.train(t1);

        PlattCalibration result = instance.clone();

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), result.classify(t1.getDataPoint(i)).mostLikely());

        result.train(t2);

        for (int i = 0; i < t1.size(); i++)
            assertEquals(t1.getDataPointCategory(i), instance.classify(t1.getDataPoint(i)).mostLikely());

        for (int i = 0; i < t2.size(); i++)
            assertEquals(t2.getDataPointCategory(i), result.classify(t2.getDataPoint(i)).mostLikely());
    }

}
