package org.op_ra.listeners;

import org.op_ra.utils.DataProviderUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * {@code AnnotationTransformer} is a TestNG listener that implements the {@link IAnnotationTransformer}
 * interface. This listener allows for the modification of TestNG annotations at runtime, providing a
 * powerful mechanism for dynamically configuring tests.
 *
 * <p>In this framework, this listener is primarily used to set the retry analyzer for all test methods.
 * This ensures that any failed test will be automatically retried according to the logic defined in
 * the {@link RetryFailedTests} class.</p>
 *
 * <p><b>Usage:</b></p>
 * <p>To use this listener, it must be specified in the TestNG suite XML file.</p>
 *
 * <pre>{@code
 * <suite name="MyTestSuite">
 *     <listeners>
 *         <listener class-name="org.op_ra.listeners.AnnotationTransformer"/>
 *     </listeners>
 *     <test name="MyTest">
 *         ...
 *     </test>
 * </suite>
 * }</pre>
 *
 * @see IAnnotationTransformer
 * @see RetryFailedTests
 * @see DataProviderUtils
 * @version 1.0
 * @since 2023-10-27
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /**
     * This method is called for every test method and allows for the modification of its annotations.
     * In this implementation, it sets the retry analyzer for each test method to {@link RetryFailedTests}.
     *
     * <p>The data provider annotations are currently commented out but can be enabled to set a default
     * data provider for all test methods.</p>
     *
     * @param annotation The annotation of the test method.
     * @param testClass The class of the test method.
     * @param testConstructor The constructor of the test class.
     * @param testMethod The test method.
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // annotation.setDataProvider("getJsonData");
        // annotation.setDataProviderClass(DataProviderUtils.class);
        annotation.setRetryAnalyzer(RetryFailedTests.class);
    }
}
