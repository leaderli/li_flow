package com.leaderli.li.flow.util;

import java.util.List;

import org.eclipse.core.resources.IProject;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class FlowNodeUtil {

	public static void updateGotoNodeAfterSetFlowName(IProject project, FlowNode flowNode) {

		String flowName = flowNode.getFlowName();
		FlowDiagram flowDiagram = flowNode.getParent();

		flowNode.getGotoNodes().clear();

		List<String> returnNodes = LiPlugin.getDefault().getCallflowController().getFlowSubFlowReturnNodes(project, flowName);

		returnNodes.forEach(nodeName -> {

			GotoNode gotoNode = new GotoNode();
			gotoNode.setId(flowDiagram.spanningNextNodeID());
			gotoNode.setName(nodeName);
			flowNode.addGotoNode(gotoNode);
		});

	}
}
