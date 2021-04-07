package com.leaderli.li.flow.editor;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leaderli.li.flow.editor.action.SelectionContextMenuAction;
import com.leaderli.li.flow.util.GEFUtil;

public class FLowEditorContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;

	public FLowEditorContextMenuProvider(final ActionRegistry actionRegistry, EditPartViewer viewer) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

	private static class TestAction extends Action {
		private String editorFile;
		private String nodeName;

		public TestAction(String editorFile, String nodeName) {
			this.editorFile = editorFile;
			this.nodeName = nodeName;
			setText("Edit");
		}

		@Override
		public void run() {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			;
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorInput input = page.getActiveEditor().getEditorInput();
			IResource activeResource = input.getAdapter(IResource.class);

			IProject iproject = activeResource.getProject();

			String packageName = editorFile;
			IFolder iFolder = iproject
					.getFolder("src" + File.separatorChar + packageName.replace('.', File.separatorChar));
			if (!iFolder.exists()) {
				try {
					iFolder.create(false, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			IFile iFile = iFolder.getFile(nodeName + ".java");
			if (!iFile.exists()) {
				try {
					iFile.create(
							new ByteArrayInputStream(
									("package " + packageName + ";\r\npublic class " + nodeName + "{\r\n}").getBytes()),
							false, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}

			try {
				IDE.openEditor(page, iFile);
			} catch (PartInitException e) {
				e.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildContextMenu(IMenuManager menu) {

		GEFActionConstants.addStandardActionGroups(menu);

		EditPart editPart = getViewer().findObjectAt(GEFUtil.getCursorPointAtDiagram(getViewer()));

		actionRegistry.getActions().forEachRemaining(action -> {
			if (action instanceof SelectionContextMenuAction) {
				SelectionContextMenuAction.registrySelectionContextMenu(menu, (SelectionContextMenuAction<?>) action,
						editPart);
			}

		});
		;
	}

}