package com.leaderli.li.flow.wizard;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.Location;
import com.leaderli.li.flow.editor.serialize.SerializeUtil;
import com.leaderli.li.flow.util.GEFUtil;
import com.leaderli.li.flow.util.GsonUtil;

public class CreateFlowWizard extends Wizard implements INewWizard {
	IWorkbench workbench;
	IStructuredSelection selection;
	IProject project;
	IFolder flowFolder;
	public CreateFlowWizard() {
		setWindowTitle("New Flow");
	}

	/**
	 * wizard可能有多个操作页面，当点击next时会调用getNextPage方法来返回下一个操作页面。
	 * 
	 */
	@Override
	public void addPages() {
		project = getProjectFromSelection(selection);
		// 未选中项目时，禁止一切操作，并提示相关信息
		if (project == null) {
			addPage(new ErrorMessageWizardPage());
		} else {
			flowFolder = GEFUtil.getFlowFolder(project);
			addPage(new NewFlowWizardPage(this));
		}
	}

	/**
	 * 是否禁用next和previous按钮
	 */
	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	private IProject getProjectFromSelection(IStructuredSelection selection) {
		IProject result = null;
		if (selection.size() == 1) {
			final Object firstSelectionElement = selection.getFirstElement();
			if (firstSelectionElement instanceof IJavaProject) {
				final IJavaProject javaProject = (IJavaProject) firstSelectionElement;
				result = javaProject.getProject();
			} else if (firstSelectionElement instanceof IResource) {
				IResource resource = (IResource) firstSelectionElement;
				return resource.getProject();
			}
		}
		return result;
	}




	private String getFileContent(String fileName) {

		FlowDiagram flowDiagram = new FlowDiagram();
		flowDiagram.setNextNodeID(1);
		flowDiagram.setConnectionNodes(new ArrayList<ConnectionNode>());
		flowDiagram.setFlowNodes(new ArrayList<FlowNode>());
		flowDiagram.setPackageName(PluginConstant.SUBFLOW_SOURCE_BASE_PACKAGE + fileName);
		
		
		FlowNode begin = new FlowNode();
		begin.setId(flowDiagram.spanningNextNodeID());
		begin.setName(PluginConstant.FLOW_BEGIN);
		begin.setPoint(new Location(25, 25));
		begin.setIcon(PluginConstant.ICON_SUBFLOW_ENTRY);
		begin.setType(PluginConstant.TYPE_SUBFLOW_ENTRY);
		GotoNode gotoNode = new GotoNode();
		gotoNode.setId(flowDiagram.spanningNextNodeID());
		gotoNode.setName("default");
		begin.addGotoNode(gotoNode);
		
	
		flowDiagram.addFlowNode(begin);
		return GsonUtil.toJson(SerializeUtil.transfer(flowDiagram));
	}

	/**
	 * 当点击finish按钮时，生成新建的文件
	 */
	@Override
	public boolean performFinish() {
		String fileName = getNamePage().flowName.getText();
		IFile file = GEFUtil.getFlowFile(project, fileName);
		try {
			String content = getFileContent(fileName);
			file.create(new ByteArrayInputStream(content.getBytes()), false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		try {
			IDE.openEditor(page, file);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return true;
	}

	private NewFlowWizardPage getNamePage() {
		return (NewFlowWizardPage) getPage(NewFlowWizardPage.PAGE_NAME);

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	private class ErrorMessageWizardPage extends WizardPage {
		public static final String PAGE_NAME = "ErrorMessageWizardPage";

		protected ErrorMessageWizardPage() {
			super(PAGE_NAME, "Create a new flow", null);
		}

		@Override
		public void createControl(Composite parent) {
			setControl(parent);
			this.setMessage("must select a project", IMessageProvider.ERROR);
			setPageComplete(false);
		}

	}


}
