/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.kotlinp.test

import com.intellij.openapi.Disposable
import junit.framework.TestCase.assertEquals
import kotlinx.metadata.jvm.KotlinClassFile
import org.jetbrains.kotlin.checkers.setupLanguageVersionSettingsForCompilerTests
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.codegen.GenerationUtils
import org.jetbrains.kotlin.kotlinp.Kotlinp
import org.jetbrains.kotlin.test.ConfigurationKind
import org.jetbrains.kotlin.test.KotlinTestUtils
import org.jetbrains.kotlin.test.TestJdkKind
import java.io.File
import kotlin.test.fail

fun compileAndPrintAllFiles(file: File, disposable: Disposable, tmpdir: File, compareWithTxt: Boolean, readWriteAndCompare: Boolean) {
    val read = StringBuilder()
    val readWriteRead = StringBuilder()

    compile(file, disposable, tmpdir) { outputFile ->
        when (outputFile.extension) {
            "kotlin_module" -> {
                val moduleFile = Kotlinp.readModuleFile(outputFile)
                // TODO: transformModuleFile
                for ((sb, moduleFileToRender) in listOf(read to moduleFile, readWriteRead to moduleFile)) {
                    sb.appendFileName(outputFile.relativeTo(tmpdir))
                    sb.append(Kotlinp.renderModuleFile(moduleFileToRender))
                }
            }
            "class" -> {
                val classFile = Kotlinp.readClassFile(outputFile)!!
                val classFile2 = transformClassFile(classFile)

                for ((sb, classFileToRender) in listOf(read to classFile, readWriteRead to classFile2)) {
                    sb.appendFileName(outputFile.relativeTo(tmpdir))
                    sb.append(Kotlinp.renderClassFile(classFileToRender))
                }
            }
            else -> fail("Unknown file: $outputFile")
        }
    }

    if (compareWithTxt) {
        KotlinTestUtils.assertEqualsToFile(File(file.path.replace(".kt", ".txt")), read.toString())
    }

    if (readWriteAndCompare) {
        assertEquals(read.toString(), readWriteRead.toString())
    }
}

private fun compile(file: File, disposable: Disposable, tmpdir: File, forEachOutputFile: (File) -> Unit) {
    val environment = KotlinCoreEnvironment.createForTests(
        disposable,
        KotlinTestUtils.newConfiguration(ConfigurationKind.ALL, TestJdkKind.MOCK_JDK),
        EnvironmentConfigFiles.JVM_CONFIG_FILES
    )
    setupLanguageVersionSettingsForCompilerTests(file.readText(), environment)
    val ktFile = KotlinTestUtils.createFile(file.name, file.readText(), environment.project)
    GenerationUtils.compileFileTo(ktFile, environment, tmpdir)

    for (outputFile in tmpdir.walkTopDown().sortedBy { it.nameWithoutExtension }) {
        if (outputFile.isFile) {
            forEachOutputFile(outputFile)
        }
    }
}

private fun StringBuilder.appendFileName(file: File) {
    appendln("// $file")
    appendln("// ------------------------------------------")
}

// Reads the class file and writes it back with *Writer visitors.
// The resulting class file should be the same from the point of view of any metadata reader, including kotlinp
// (the exact bytes may differ though, because there are multiple ways to encode the same metadata)
private fun transformClassFile(classFile: KotlinClassFile): KotlinClassFile =
    when (classFile) {
        is KotlinClassFile.Class -> KotlinClassFile.Class.Writer().apply(classFile::accept).write()
        is KotlinClassFile.FileFacade -> KotlinClassFile.FileFacade.Writer().apply(classFile::accept).write()
        is KotlinClassFile.Lambda -> KotlinClassFile.Lambda.Writer().apply(classFile::accept).write()
        is KotlinClassFile.SyntheticClass -> KotlinClassFile.SyntheticClass.Writer().write()
        is KotlinClassFile.MultiFileClassFacade -> KotlinClassFile.MultiFileClassFacade.Writer().write(classFile.partClassNames)
        is KotlinClassFile.MultiFileClassPart ->
            KotlinClassFile.MultiFileClassPart.Writer().apply(classFile::accept).write(classFile.facadeClassName)
        else -> classFile
    }
