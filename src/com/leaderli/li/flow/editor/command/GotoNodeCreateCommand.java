package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.FlowNodeObejctTreeEditor;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class GotoNodeCreateCommand extends CommandExecuteInCommandStack<GotoNode, FlowNode, FlowNodeObejctTreeEditor> {

	private FlowNode parent;

	@Override
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


}
