package com.leaderli.li.flow.generate;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class TemplateMethodDeclaration {

	/**
	 * 
	 * 查找方法public Map<String, String> getBranches()
	 */
	
	public static boolean isGetBranches(MethodDeclaration node) {
	
		int modifiers = node.getModifiers();
		if (!Modifier.isPublic(modifiers)) {
			return false;
		}
		if (!node.getName().getFullyQualifiedName().equals("getBranches")) {
			return false;
		}
		if (node.getReceiverType() != null) {
			return false;
		}
		return node.getReturnType2().toString().equals("Map<String,String>");
	}

}
