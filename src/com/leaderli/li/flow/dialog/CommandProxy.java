package com.leaderli.li.flow.dialog;

import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.command.ModelCommand;
import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public abstract class CommandProxy<T extends Node<? extends R>, R extends NodeNotify> extends Command {

	protected boolean didExecute;
	protected ModelCommand<T, R> commandProxy;

	public CommandProxy(ModelCommand<T, R> command) {
		this.commandProxy = command;


	}
	
	@Override
	public void execute() {
		this.commandProxy.execute();
		this.didExecute = true;
	}

	@Override
	public boolean canExecute() {
		return this.commandProxy.canExecute();
	}

	@Override
	public boolean canUndo() {
		return this.commandProxy.canUndo();
	}

	@Override
	public void dispose() {
		this.commandProxy.dispose();
	}

	@Override
	public String getDebugLabel() {
		return this.commandProxy.getDebugLabel();
	}

	@Override
	public String getLabel() {
		return this.commandProxy.getLabel();
	}

	@Override
	public void setDebugLabel(String label) {
		this.commandProxy.setDebugLabel(label);
	}

	@Override
	public void setLabel(String label) {
		this.commandProxy.setLabel(label);
	}

	@Override
	public String toString() {
		return this.commandProxy.toString();
	}

	@Override
	public void undo() {
		if (this.didExecute) {
			this.commandProxy.undo();
		}
	}

	@Override
	public void redo() {
		if (this.didExecute) {
			this.commandProxy.redo();
		}
	}
}
