package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.FlowEditorProvider;
import com.leaderli.li.flow.editor.part.GenericsEditPart;

public class GenericsComponentEditPolicy<N extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette, G extends GenericsEditPart<N, F>> extends ComponentEditPolicy
		implements FlowEditorProvider<F> {

	protected G host;

	@SuppressWarnings("unchecked")
	@Override
	public void setHost(EditPart host) {
		this.host = (G) host;
	}
	@Override
	public G getHost() {
		return this.host;
	}

	public N getModel() {
		return host.getModel();
	}

	@Override
	public F getEditor() {
		return this.host.getEditor();
	}



}
