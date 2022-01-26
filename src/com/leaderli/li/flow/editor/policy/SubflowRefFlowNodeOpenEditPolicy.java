package com.leaderli.li.flow.editor.policy;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leaderli.li.flow.util.GEFUtil;

public class SubflowRefFlowNodeOpenEditPolicy extends FlowEditorOpenRoleEditPolicy {

	@Override
	protected Runnable executor() {
		return () -> {
			IProject project = getHost().getProject();
			IFile file = GEFUtil.getFlowFolder(project).getFile(getHost().getModel().getFlowName() + GEFUtil.FLOW_EXTENSION);
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			try {
				IDE.openEditor(page, file);
			} catch (PartInitException e) {
				e.printStackTrace();
			}

		};
	}

}
