package com.leaderli.li.flow.generate;

import java.text.MessageFormat;

import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.util.ModelUtil;
import com.leaderli.li.flow.util.ResourcesUtil;

public class GenerateMethod {


	public static String getBranches(FlowNode flowNode) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t\tMap<String,String> branches = new HashMap<>();");
		sb.append("\n");
		FlowDiagram flowDiagram = flowNode.getParent();
		flowNode.getGotoNodes().forEach(got -> {
			String key = got.getName();
			FlowNode target = ModelUtil.getLinkedFlowNode(got);
			String value = target == null ? "" : ResourcesUtil.getSimpleName(flowDiagram.getPackageName()) + "-" + target.getName();
			sb.append(MessageFormat.format("\t\tbranches.put(\"{0}\",\"{1}\");\n", key, value));
		});
		sb.append("\t\treturn branches;");

		return sb.toString();
	}

	public static String getExitPoints(FlowNode flowNode) {

		StringBuilder sb = new StringBuilder();
		sb.append("\t\tMap<String,String> exitPoints = new HashMap<>();");
		sb.append("\n");
		FlowDiagram flowDiagram = flowNode.getParent();
		flowNode.getGotoNodes().forEach(got -> {
			String key = got.getName();
			FlowNode target = ModelUtil.getLinkedFlowNode(got);
			String value = target == null ? "" : flowNode.getFlowName() + "-" + target.getName();
			sb.append(MessageFormat.format("\t\texitPoints.put(\"{0}\",\"{1}\");\n", key, value));
		});
		sb.append("\t\treturn exitPoints;");

		return sb.toString();
	}

	public static String getSubflowName(FlowNode flowNode) {
		return "\t\treturn  \"" + flowNode.getFlowName() + "\";";
	}
}
