package com.leaderli.li.flow.editor.command;

import org.eclipse.core.runtime.Assert;

import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public abstract class CommandExecuteInCommandStack<T extends Node<? extends R>, R extends NodeNotify> extends ModelCommand<T, R> {

	protected FlowEditor flowEditor;
	
	public abstract void initFlowEditor();
	public void dispatch() {
		initFlowEditor();
		Assert.isNotNull(flowEditor, "must set flowEditor");
		flowEditor.getEditDomain().getCommandStack().execute(this);
	}

}
