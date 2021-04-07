package com.leaderli.li.flow.editor.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leaderli.li.flow.editor.model.FlowNode;

public class EditRelateJavaAction extends SelectionContextMenuAction<FlowNode> {

	public static final String EIDT_RELATE_JAVA = "EditRelateJavaAction";

	public EditRelateJavaAction() {
		this.setId(EditRelateJavaAction.EIDT_RELATE_JAVA);
		this.setText("Edit");

	}

	@Override
	public void run() {

		IFile iFile = (IFile) this.editPart.getAdapter(IFile.class);

		try {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IDE.openEditor(page, iFile);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

}
