package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.GotoNode;

public class ConnectionNodeReconnectSourceCommand extends ModelCommand<ConnectionNode,FlowDiagram> {

	private GotoNode oldSource, newSource;



	@Override
	public void execute() {

		if (oldSource == null) {
			oldSource = getModel().getParent().getRegisterNode(getModel().getSourceID());
		}
		getModel().setSourceID(newSource.getId());
		newSource.setLinkedConnectionNode(getModel().getId());
		oldSource.setLinkedConnectionNode(PluginConstant.NO_LINKED_CONNECTION_NODE);
		
		getModel().notifyChanged();
		
	}

	@Override
	public void undo() {
		getModel().setSourceID(oldSource.getId());
		oldSource.setLinkedConnectionNode(getModel().getId());
		newSource.setLinkedConnectionNode(PluginConstant.NO_LINKED_CONNECTION_NODE);

		getModel().notifyChanged();

	}

	public void setNewSource(GotoNode newSource) {
		this.newSource = newSource;
	}
}
