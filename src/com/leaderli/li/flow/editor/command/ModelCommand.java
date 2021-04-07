package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public class ModelCommand<T extends Node<? extends R>, R extends NodeNotify> extends Command {

	T model;
	R parent;

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

	public R getParent() {
		return parent;
	}

	public void setParent(R parent) {
		this.parent = parent;
	}

	@Override
	public boolean canExecute() {
		return model != null;
	}


}
