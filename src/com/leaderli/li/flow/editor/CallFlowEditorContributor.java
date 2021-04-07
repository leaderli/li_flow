package com.leaderli.li.flow.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

public class CallFlowEditorContributor extends MultiPageEditorActionBarContributor {

	private List<RetargetAction> retargetActions = new ArrayList<>();
	private ActionRegistry registry = new ActionRegistry();
	private List<String> globalActionKeys = new ArrayList<>();

	private ActionRegistry getActionRegistry() {
		return this.registry;
	}

	private void addAction(RetargetAction action) {
		this.getActionRegistry().registerAction(action);

	}

	@Override
	public void init(IActionBars bars) {
		this.buildActions();
		this.declareGlobalActionKeys();
		super.init(bars);
	}

	private void addGlobalActionKey(String id) {
		this.globalActionKeys.add(id);

	}
	private void addRetargetAction(RetargetAction action) {
		this.addAction(action);
		this.retargetActions.add(action);
		this.getPage().addPartListener(action);
		this.addGlobalActionKey(action.getId());
	}

	@Override
	public void dispose() {
		this.retargetActions.forEach(action -> {
			this.getPage().removePartListener(action);
		});
		this.registry.dispose();
		this.retargetActions = null;
		this.registry = null;
	}

	private IAction getAction(String id) {
		return this.getActionRegistry().getAction(id);
	}

	@Override
	public void setActivePage(IEditorPart activeEditor) {
		IActionBars bars = this.getActionBars();
		if (activeEditor != null && bars != null) {
			ActionRegistry registry = activeEditor.getAdapter(ActionRegistry.class);
			this.globalActionKeys.forEach(id -> {
				bars.setGlobalActionHandler(id, registry.getAction(id));
			});

			bars.updateActionBars();
			IWorkbenchPage page = activeEditor.getSite().getPage();
		}



	}

	protected void buildActions() {
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new DeleteRetargetAction());
	}





	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
	}



	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(ActionFactory.DELETE.getId()));

	}


}