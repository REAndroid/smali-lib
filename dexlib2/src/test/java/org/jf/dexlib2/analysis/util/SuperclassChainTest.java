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

package org.jf.dexlib2.analysis.util;

import junit.framework.Assert;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.analysis.DexClassProvider;
import org.jf.dexlib2.analysis.TestUtils;
import org.jf.dexlib2.analysis.TypeProto;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.immutable.ImmutableDexFile;
import org.jf.util.collection.ArraySet;
import org.jf.util.collection.ListUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class SuperclassChainTest {

    @Test
    public void testGetSuperclassChain() throws IOException {
        ClassDef objectClassDef = TestUtils.makeClassDef("Ljava/lang/Object;", null);
        ClassDef oneClassDef = TestUtils.makeClassDef("Ltest/one;", "Ljava/lang/Object;");
        ClassDef twoClassDef = TestUtils.makeClassDef("Ltest/two;", "Ltest/one;");
        ClassDef threeClassDef = TestUtils.makeClassDef("Ltest/three;", "Ltest/two;");

        Set<ClassDef> classes = ArraySet.<ClassDef>of(
                objectClassDef, oneClassDef, twoClassDef, threeClassDef);

        ClassPath classPath = new ClassPath(new DexClassProvider(new ImmutableDexFile(Opcodes.getDefault(), classes)));

        TypeProto objectClassProto = classPath.getClass("Ljava/lang/Object;");
        TypeProto oneClassProto = classPath.getClass("Ltest/one;");
        TypeProto twoClassProto = classPath.getClass("Ltest/two;");
        TypeProto threeClassProto = classPath.getClass("Ltest/three;");

        Assert.assertEquals(
                ListUtil.<TypeProto>of(),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(objectClassProto)));

        Assert.assertEquals(
                ListUtil.<TypeProto>of(objectClassProto),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(oneClassProto)));

        Assert.assertEquals(
                ListUtil.<TypeProto>of(oneClassProto, objectClassProto),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(twoClassProto)));

        Assert.assertEquals(
                ListUtil.<TypeProto>of(twoClassProto, oneClassProto, objectClassProto),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(threeClassProto)));
    }

    @Test
    public void testGetSuperclassChain_Unresolved() throws IOException {
        // Ltest/one; isn't defined

        ClassDef twoClassDef = TestUtils.makeClassDef("Ltest/two;", "Ltest/one;");
        ClassDef threeClassDef = TestUtils.makeClassDef("Ltest/three;", "Ltest/two;");
        Set<ClassDef> classes = ArraySet.<ClassDef>of(twoClassDef, threeClassDef);
        ClassPath classPath = new ClassPath(new DexClassProvider(new ImmutableDexFile(Opcodes.getDefault(), classes)));

        TypeProto unknownClassProto = classPath.getUnknownClass();
        TypeProto oneClassProto = classPath.getClass("Ltest/one;");
        TypeProto twoClassProto = classPath.getClass("Ltest/two;");
        TypeProto threeClassProto = classPath.getClass("Ltest/three;");

        Assert.assertEquals(
                ListUtil.<TypeProto>of(oneClassProto, unknownClassProto),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(twoClassProto)));

        Assert.assertEquals(
                ListUtil.<TypeProto>of(twoClassProto, oneClassProto, unknownClassProto),
                ListUtil.copyOf(TypeProtoUtils.getSuperclassChain(threeClassProto)));
    }
}
