package com.leaderli.li.flow.editor.policy;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PartInitException;

import com.leaderli.li.flow.editor.CallFlowEditor;

public class ServletFlowNodeOpenEditPolicy extends FlowEditorOpenRoleEditPolicy {

	@Override
	protected Runnable executor() {
		return () -> {
			CallFlowEditor callFlowEditor = getCallflowEditor();
			if (callFlowEditor != null) {
				try {
					callFlowEditor.openServletFlowNodeItemEditorPage(getHost().getModel(), true);
					getHost().setSelected(0);
					getHost().getViewer().setSelection(new StructuredSelection());
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		};
	}


}
