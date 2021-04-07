package com.leaderli.li.flow.editor.action;

import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import com.leaderli.li.flow.editor.part.FlowNodeEditPart;

public class FlowNodeSelectionAction extends SelectionAction {

	public static final String OPEN_REQUEST = "open";
	public FlowNodeSelectionAction(IWorkbenchPart part) {
		super(part);
		this.setId("OpenNodeAction");
	}

	@Override
	protected boolean calculateEnabled() {

		return true;
	}

	@Override
	public void run() {
		super.run();
		System.out.println("open request ");
		getSelectedFlowNodeEditPart().performRequest(new Request(RequestConstants.REQ_OPEN));
	}

	@Override
	protected void handleSelectionChanged() {
	}

	protected FlowNodeEditPart getSelectedFlowNodeEditPart() {
		ISelection selection = this.getSelection();

		if (selection != null && (selection instanceof IStructuredSelection)) {
			List<?> selections = ((IStructuredSelection) selection).toList();

			if (selections.size() == 1) {
				Object editPart = selections.get(0);
				if (editPart instanceof FlowNodeEditPart) {
					return (FlowNodeEditPart) editPart;
				}
			}

		}

		return null;
	}

}
