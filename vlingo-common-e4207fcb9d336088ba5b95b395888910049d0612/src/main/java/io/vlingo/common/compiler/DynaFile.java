// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.common.compiler;

import java.io.File;
import java.io.FileOutputStream;

public class DynaFile {
  public static final String GeneratedSources = "target/generated-sources/";
  public static final String GeneratedTestSources = "target/generated-test-sources/";
  public static final String RootOfMainClasses = "target/classes/";
  public static final String RootOfTestClasses = "target/test-classes/";
  
  public static File persistDynaClass(final String pathToClass, final byte[] dynaClass) throws Exception {
    final File pathToClassFile = new File(pathToClass);
    
    try (final FileOutputStream stream = new FileOutputStream(pathToClassFile)) {
      stream.write(dynaClass);
      return pathToClassFile;
    }
  }

  public static File persistDynaClassSource(final String pathToSource, final String dynaClassSource) throws Exception {
    final File pathToSourceFile = new File(pathToSource);
    
    try (final FileOutputStream stream = new FileOutputStream(pathToSourceFile)) {
      stream.write(dynaClassSource.getBytes());
      return pathToSourceFile;
    }
  }

  public static String toFullPath(final String fullyQualifiedClassname) {
    return toPath(fullyQualifiedClassname, true);
  }

  public static String toPackagePath(final String fullyQualifiedClassname) {
    return toPath(fullyQualifiedClassname, false);
  }

  private static String toPath(final String fullyQualifiedClassname, final boolean includeClassname) {
    final String[] names = fullyQualifiedClassname.split("\\.");
    boolean first = true;
    
    final int actualLength = names.length - (includeClassname ? 0:1);
    
    final StringBuilder builder = new StringBuilder();
    
    for (int idx = 0; idx < actualLength; ++idx) {
      if (!first) {
        builder.append("/");
      }
      builder.append(names[idx]);
      first = false;
    }
    
    return builder.toString();
  }
}
