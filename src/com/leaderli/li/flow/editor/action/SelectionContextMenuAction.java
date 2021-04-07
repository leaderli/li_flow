package com.leaderli.li.flow.editor.action;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.editor.part.GenericsEditPart;
import com.leaderli.li.flow.util.ClassUtil;

public class SelectionContextMenuAction<T extends NodeNotify> extends WorkbenchPartAction {

	protected Class<T> type;
	protected GenericsEditPart<T> editPart;

	public SelectionContextMenuAction() {
		super(null);
		type = ClassUtil.getGenericSuperclassActualTypeArgument(this);

	}

	@Override
	public void setWorkbenchPart(IWorkbenchPart part) {
		super.setWorkbenchPart(part);
	}

	public void setEditPart(GenericsEditPart<T> editPart) {
		this.editPart = editPart;
	}
	
	public  T getModel() {
		return this.editPart.getModel();
	}

	@Override
	public boolean calculateEnabled() {
		return editPart != null && type == editPart.getModel().getClass();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void registrySelectionContextMenu(IMenuManager menu, SelectionContextMenuAction<?> action,
			EditPart editPart) {
		if (editPart instanceof GenericsEditPart) {
			action.setEditPart((GenericsEditPart) editPart);
			if (action.calculateEnabled()) {
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
			}
		}

	}

}
