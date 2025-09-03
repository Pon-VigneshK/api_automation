package org.op_ra.listeners;

import org.op_ra.utils.DataProviderUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Implements {@link org.testng.IAnnotationTransformer} to leverage specific functionality, such as updating the annotations of test
 * methods at runtime.
 *
 * @see DataProviderUtils
 */

public class AnnotationTransformer implements IAnnotationTransformer {
    /**
     * Assists in setting the dataProvider, dataProvider class, and retry analyzer annotations for all the test methods at runtime.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
//        annotation.setDataProvider("getJsonData");
//        annotation.setDataProviderClass(DataProviderUtils.class);
        annotation.setRetryAnalyzer(RetryFailedTests.class);
    }
}
