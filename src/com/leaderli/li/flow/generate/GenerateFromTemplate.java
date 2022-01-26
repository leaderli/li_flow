package com.leaderli.li.flow.generate;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

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
			replaces.put("$" + key, value);
			return this;
		}

		public String build(IProject project) {

			result = ResourcesUtil.getFlowNodeSourceCodeTemplate(project, template);
			replaces.forEach((k, v) -> {
				result = result.replace(k, v);

			});
			return result;
		}

	}
}
