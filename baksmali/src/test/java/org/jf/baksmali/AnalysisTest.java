/*
 * Copyright 2013, Google Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jf.baksmali;

import org.jf.baksmali.Adaptors.ClassDefinition;
import org.jf.baksmali.formatter.BaksmaliWriter;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.util.io.ByteStreams;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AnalysisTest {

    @Test
    public void ConstructorTest() throws IOException, URISyntaxException {
        runTest("ConstructorTest", true);
    }

    @Test
    public void RegisterEqualityOnMergeTest() throws IOException, URISyntaxException {
        runTest("RegisterEqualityOnMergeTest", true);
    }

    @Test
    public void UninitRefIdentityTest() throws IOException, URISyntaxException {
        runTest("UninitRefIdentityTest", true);
    }

    @Test
    public void InstanceOfTest() throws IOException, URISyntaxException {
        runTest("InstanceOfTest", true, true);
    }

    @Test
    public void MultipleStartInstructionsTest() throws IOException, URISyntaxException {
        runTest("MultipleStartInstructionsTest", true);
    }

    @Test
    public void DuplicateTest() throws IOException, URISyntaxException {
        runTest("DuplicateTest", false);
    }

    @Test
    public void LocalTest() throws IOException, URISyntaxException {
        runTest("LocalTest", false);
    }

    public void runTest(String test, boolean registerInfo) throws IOException, URISyntaxException {
        runTest(test, registerInfo, false);
    }

    public void runTest(String test, boolean registerInfo, boolean isArt) throws IOException, URISyntaxException {
        String dexFilePath = String.format("%s%sclasses.dex", test, File.separatorChar);

        DexFile dexFile = DexFileFactory.loadDexFile(findResource(dexFilePath), Opcodes.getDefault());

        BaksmaliOptions options = new BaksmaliOptions();
        if (registerInfo) {
            options.registerInfo = BaksmaliOptions.ALL | BaksmaliOptions.FULLMERGE;
            if (isArt) {
                options.classPath = new ClassPath(new ArrayList<>(), true, 56);
            } else {
                options.classPath = new ClassPath();
            }
        }
        options.implicitReferences = false;

        for (ClassDef classDef: dexFile.getClasses()) {
            StringWriter stringWriter = new StringWriter();
            BaksmaliWriter writer = new BaksmaliWriter(stringWriter);
            ClassDefinition classDefinition = new ClassDefinition(options, classDef);
            classDefinition.writeTo(writer);
            writer.close();

            String className = classDef.getType();
            String smaliPath = String.format("%s%s%s.smali", test, File.separatorChar,
                    className.substring(1, className.length() - 1));
            String smaliContents = readResource(smaliPath);

            Assert.assertEquals(BaksmaliTestUtils.normalizeWhitespace(smaliContents),
                    BaksmaliTestUtils.normalizeWhitespace((stringWriter.toString())));
        }
    }

    @Nonnull
    private File findResource(String resource) throws URISyntaxException {
        ClassLoader loader = getClass().getClassLoader();
        URL resUrl = loader.getResource(resource);
        if(resUrl == null){
            throw new URISyntaxException("Failed to find ULR of resource: ", resource);
        }
        return new File(resUrl.toURI());
    }

    @Nonnull
    private String readResource(String resource) throws IOException {
        InputStream inputStream = null;
        try {
            ClassLoader loader = getClass().getClassLoader();
            inputStream = loader.getResourceAsStream(resource);
        } catch (Exception ignored) {
        }
        if(inputStream == null){
            try {
                File file = findResource(resource);
                if(file.isFile()){
                    inputStream = new FileInputStream(file);
                }
            } catch (URISyntaxException ignored) {
            }
        }
        if(inputStream == null){
            throw new IOException("Failed to find resource: " + resource);
        }
        byte[] bytes = ByteStreams.toByteArray(inputStream);
        return new String(bytes, 0, bytes.length, StandardCharsets.UTF_8);
    }
}
