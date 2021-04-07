package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;

public class ConnectionNodeDeleteCommand extends ModelCommand<ConnectionNode,FlowDiagram> {


	@Override
	public void execute() {
		model.getParent().removeConnectionNode(model);
	}

	@Override
	public void undo() {
		model.getParent().addConnectionNode(model);
	}

}
