package com.leaderli.li.flow.editor.action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.GenericsEditPart;

public class SelectionContextMenuAction<N extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette, T extends GenericsEditPart<N, F>> extends WorkbenchPartAction {

	protected T editPart;

	public SelectionContextMenuAction() {
		super(null);
	}

	@Override
	public void setWorkbenchPart(IWorkbenchPart part) {
		super.setWorkbenchPart(part);
	}

	public void setEditPart(T editPart) {
		this.editPart = editPart;
	}

	public N getModel() {
		return this.editPart.getModel();
	}



	@Override
	public boolean calculateEnabled() {
		return editPart != null;
	}

	@SuppressWarnings("unchecked")
	public static <N extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette> void registrySelectionContextMenu(IMenuManager menu,
			SelectionContextMenuAction<N, F, GenericsEditPart<N, F>> action,
			EditPart editPart) {
		if (editPart instanceof GenericsEditPart) {
			action.setEditPart((GenericsEditPart<N, F>) editPart);
			if (action.calculateEnabled()) {
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			}
		}

	}


}
