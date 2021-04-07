package com.leaderli.li.flow.editor.part;

import org.eclipse.gef.EditPart;

import com.leaderli.li.flow.constant.PluginConstant;

public class EditPartFactoryUtil {



	public static EditPart getFlowNodeEditPartByType(String type) {
		EditPart editPart = null;

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
