package com.leaderli.li.flow.generate;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.leaderli.li.flow.util.ResourcesUtil;

public class GenerateFromTemplate {

	public static Builder template(String templateName) {

		Builder builder = new Builder();
		builder.template = templateName;
		return builder;
	}

	public static class Builder {

		private String template;
		private String result;
		private Map<String, String> replaces = new HashMap<>();

		public Builder addPlaceHolder(String key, String value) {
			this.replaces.put("$" + key, value);
			return this;
		}

		public String build(IProject project) {

			this.result = ResourcesUtil.getFlowNodeSourceCodeTemplate(project, this.template);
			this.replaces.forEach((k, v) -> {
				this.result = this.result.replace(k, v);

			});
			return this.result;
		}

	}

	/**
	 * 
	 * 查找方法public Map<String, String> getBranches()
	 */

	public static boolean getBranchesMethod(MethodDeclaration node) {
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
