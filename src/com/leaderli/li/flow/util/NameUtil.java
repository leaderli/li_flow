package com.leaderli.li.flow.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;

public class NameUtil {

	public static IStatus validateJavaTypeName(String name) {
		return JavaConventions.validateJavaTypeName(name, "1.8", "1.8");
	}


}

