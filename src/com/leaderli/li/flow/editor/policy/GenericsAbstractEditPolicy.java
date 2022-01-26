package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.GenericsEditPart;

public class GenericsAbstractEditPolicy<N extends NodeNotify, R extends BaseGraphicalEditorWithFlyoutPalette, T extends GenericsEditPart<N, R>> extends AbstractEditPolicy {

	protected T host;

	@Override
	public T getHost() {
		return this.host;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setHost(EditPart host) {
		this.host = (T) host;
	}
	



}
