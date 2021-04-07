package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class GotoNodeDeleteCommand extends CommandExecuteInCommandStack<GotoNode, FlowNode> {

	private ConnectionNode linkedConnectionNode;

	@Override
	public void undo() {
		parent.addGotoNode(model);
		if (linkedConnectionNode != null) {
			parent.getParent().addConnectionNode(linkedConnectionNode);
		}
	}

	@Override
	public void execute() {
		linkedConnectionNode = parent.getParent().getRegisterNode(model.getLinkedConnectionNode());
		if (linkedConnectionNode != null) {
			parent.getParent().removeConnectionNode(linkedConnectionNode);

		}
		parent.removeGotoNode(model);
	}

	@Override
	public void initFlowEditor() {
		if (this.flowEditor == null) {
			this.flowEditor = parent.getParent().getEditor();
		}
	}

}
