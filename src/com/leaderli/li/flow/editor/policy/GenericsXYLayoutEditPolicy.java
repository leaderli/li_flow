package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.GenericsEditPart;

public abstract class GenericsXYLayoutEditPolicy<N extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette, T extends GenericsEditPart<N, F>> extends XYLayoutEditPolicy {
	protected T host;

	@SuppressWarnings("unchecked")
	@Override
	public void setHost(EditPart host) {
		this.host = (T) host;
	}

	@Override
	public T getHost() {
		return this.host;
	}

	public N getModel() {
		return host.getModel();
	}

	public F getEditor() {
		return this.host.getEditor();
	}

}
