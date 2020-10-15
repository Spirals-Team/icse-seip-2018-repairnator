// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.common.compiler;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class DynaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
  public final ByteCode byteCode;
  
  protected DynaFileManager(final Class<?> protocol, final JavaFileManager fileManager, final ClassLoader classLoader) throws Exception {
    super(fileManager);
    
    this.byteCode = new ByteCode(protocol);
  }

  @Override
  public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
    return byteCode;
  }
}
