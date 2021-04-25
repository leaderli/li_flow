package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public class ModelCommand<M extends Node<? extends P>, P extends NodeNotify> extends Command {

	M model;
	P parent;

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public P getParent() {
		return parent;
	}

	public void setParent(P parent) {
		this.parent = parent;
	}

	@Override
	public boolean canExecute() {
		return model != null;
	}


}
