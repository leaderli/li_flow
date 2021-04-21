package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.GotoNode;

public class ConnectionNodeReconnectSourceCommand extends ModelCommand<ConnectionNode,FlowDiagram> {

	private GotoNode oldSource, newSource;



	@Override
	public void execute() {

		if (oldSource == null) {
			oldSource = getModel().getSourceID();
		}
		getModel().setSourceID(newSource);
		newSource.setLinkedConnectionNode(getModel());
		oldSource.setLinkedConnectionNode(null);
		
		getModel().notifyChanged();
		
	}

	@Override
	public void undo() {
		getModel().setSourceID(oldSource);
		oldSource.setLinkedConnectionNode(getModel());
		newSource.setLinkedConnectionNode(null);

		getModel().notifyChanged();

	}

	public void setNewSource(GotoNode newSource) {
		this.newSource = newSource;
	}
}
