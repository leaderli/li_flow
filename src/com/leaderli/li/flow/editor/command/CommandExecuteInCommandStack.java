package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;
import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.FlowEditorProvider;

public abstract class CommandExecuteInCommandStack<M extends Node<? extends P>, P extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette> extends ModelCommand<M, P>
		implements FlowEditorProvider<F> {

	protected F editor;

	@Override
	public F getEditor() {
		return this.editor;
	}

	@Override
	public void setEditor(F editor) {
		this.editor = editor;
	}

	public void dispatch() {
		editor.getEditDomain().getCommandStack().execute(this);
	}

}
