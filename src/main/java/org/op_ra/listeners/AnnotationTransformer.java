package org.op_ra.listeners;

import org.op_ra.utils.DataProviderUtils; // Assuming DataProviderUtils is in utils
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Implements {@link org.testng.IAnnotationTransformer} to modify TestNG annotations at runtime.
 * This transformer specifically sets the {@code dataProvider} and {@code dataProviderClass}
 * attributes for all test methods (annotated with {@code @Test}) to use the
 * "getJsonData" data provider from {@link DataProviderUtils}.
 * <p>
 * This allows centralizing the data provider logic and avoids having to specify
 * it in every {@code @Test} annotation.
 * </p>
 * To use this transformer, it needs to be specified in the TestNG suite XML file:
 * <pre>{@code
 * <suite name="MySuite">
 *     <listeners>
 *         <listener class-name="org.op_ra.listeners.AnnotationTransformer"/>
 *     </listeners>
 *     ...
 * </suite>
 * }</pre>
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /**
     * Transforms the {@link org.testng.annotations.ITestAnnotation} of a test method.
     * This implementation sets the data provider to "getJsonData" and the data provider class
     * to {@link DataProviderUtils} for all test methods.
     *
     * @param annotation      The annotation that will be examined.
     * @param testClass       If the annotation is on a class, this parameter represents the class. Null otherwise.
     * @param testConstructor If the annotation is on a constructor, this parameter represents the constructor. Null otherwise.
     * @param testMethod      If the annotation is on a method, this parameter represents the method. Null otherwise.
     */
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        // This will apply to all @Test methods encountered by TestNG
        // if this transformer is registered.
        annotation.setDataProvider("getJsonData");
        annotation.setDataProviderClass(DataProviderUtils.class);

        // Optionally, you could add logic here to only apply the data provider
        // if certain conditions are met, e.g., if the test method has a specific annotation
        // or belongs to a particular group.
        // For example:
        // if (testMethod.getName().contains("specialDataTest")) {
        //     annotation.setDataProvider("specialDataProvider");
        // }

        // If you want to set retry analyzer:
        // annotation.setRetryAnalyzer(RetryFailedTests.class);
        // This is often done here or in MethodInterceptor.
    }
}
