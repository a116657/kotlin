/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.kotlinp.test;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("libraries/tools/kotlinp/testData")
@TestDataPath("$PROJECT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinpTestGenerated extends AbstractKotlinpTest {
    public void testAllFilesPresentInTestData() throws Exception {
        KotlinTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("libraries/tools/kotlinp/testData"), Pattern.compile("^(.+)\\.kt$"), TargetBackend.ANY, true);
    }

    @TestMetadata("Lambda.kt")
    public void testLambda() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/Lambda.kt");
        doTest(fileName);
    }

    @TestMetadata("MultiFileClass.kt")
    public void testMultiFileClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/MultiFileClass.kt");
        doTest(fileName);
    }

    @TestMetadata("NestedClasses.kt")
    public void testNestedClasses() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/NestedClasses.kt");
        doTest(fileName);
    }

    @TestMetadata("SimpleClass.kt")
    public void testSimpleClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/SimpleClass.kt");
        doTest(fileName);
    }

    @TestMetadata("SimplePackage.kt")
    public void testSimplePackage() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/SimplePackage.kt");
        doTest(fileName);
    }

    @TestMetadata("SyntheticClass.kt")
    public void testSyntheticClass() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/SyntheticClass.kt");
        doTest(fileName);
    }

    @TestMetadata("TypeParameters.kt")
    public void testTypeParameters() throws Exception {
        String fileName = KotlinTestUtils.navigationMetadata("libraries/tools/kotlinp/testData/TypeParameters.kt");
        doTest(fileName);
    }
}
