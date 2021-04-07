package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class FlowNodeRenameCommand extends ModelCommand<FlowNode, FlowDiagram> {

	private String oldName, newName;

	@Override
	public boolean canExecute() {
		return !this.newName.equals(this.oldName);

	}

	@Override
	public void execute() {

		oldName = model.getName();
		model.setName(newName);
	}

	@Override
	public void undo() {
		model.setName(oldName);
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}