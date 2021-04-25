package com.leaderli.li.flow.editor.policy;

import org.eclipse.core.runtime.Assert;

import com.leaderli.li.flow.editor.CallFlowEditor;
import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.GenericsEditPart;
import com.leaderli.li.flow.editor.part.IMultiPageEditPart;

public abstract class FlowEditorEditPolicy<N extends NodeNotify, T extends GenericsEditPart<N, FlowEditor>> extends GenericsAbstractEditPolicy<N, FlowEditor, T> {

	@Override
	public void activate() {
		if (getHost() instanceof IMultiPageEditPart) {
			super.activate();
			return;
		}
		throw new IllegalStateException("EditPolicy installed on the wrong type of edit part!  Requires IMultiPageEditPart");
	}

	protected final CallFlowEditor getCallflowEditor() {
		CallFlowEditor editor = ((IMultiPageEditPart) getHost()).getMultiPageEditor();
		Assert.isNotNull(editor);
		return editor;
	}

}
