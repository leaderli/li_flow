package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class GotoNodeCreateCommand extends CommandExecuteInCommandStack<GotoNode, FlowNode> {

	private FlowNode parent;

	public void setParent(FlowNode parent) {
		this.parent = parent;
	}

	@Override
	public void undo() {
		parent.removeGotoNode(model);
	}

	@Override
	public void execute() {

		parent.addGotoNode(model);

	}

	@Override
	public void initFlowEditor() {
		if (this.flowEditor == null) {
			this.flowEditor = parent.getParent().getEditor();
		}
	}

}
