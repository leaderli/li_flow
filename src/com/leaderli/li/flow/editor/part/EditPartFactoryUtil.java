package com.leaderli.li.flow.editor.part;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.FlowNode;

public class EditPartFactoryUtil {

	public static GenericsEditPart<FlowNode, FlowEditor> getFlowNodeEditPartByType(String type) {

		GenericsEditPart<FlowNode, FlowEditor> editPart = null;

		switch (type) {
		case PluginConstant.TYPE_SERVLET:
			editPart = new ServletFlowNodeEditPart();
			break;
		case PluginConstant.TYPE_SUBFLOW_ENTRY:
			editPart = new SubflowEntryFlowNodeEditPart();
			break;
		case PluginConstant.TYPE_SUBFLOW_REF:
			editPart = new SubflowRefFlowNodeEditPart();
			break;
		case PluginConstant.TYPE_SUBFLOW_RETURN:
			editPart = new SubflowReturnFlowNodeEditPart();
			break;
		default:
			break;
		}
		return editPart;
	}

}
