package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import com.leaderli.li.flow.editor.FlowNodeObejctTreeEditor;
import com.leaderli.li.flow.editor.command.GotoNodeDeleteCommand;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.part.GotoNodeTreeEditPart;

public class GotoNodeComponentEditPolicy extends GenericsComponentEditPolicy<GotoNode, FlowNodeObejctTreeEditor, GotoNodeTreeEditPart>
{

	@Override
	protected Command createDeleteCommand(GroupRequest request) {

		GotoNode gotoNode = getModel();

		if (gotoNode.getParent().getGotoNodes().size() == 1) {
			return null;
		}

		GotoNodeDeleteCommand delete = new GotoNodeDeleteCommand();
		delete.setModel(gotoNode);
		delete.setParent(gotoNode.getParent());
		delete.setEditor(getEditor());
		return delete;
	}


}