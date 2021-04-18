package com.leaderli.li.flow.wizard;

import java.net.URI;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.leaderli.li.flow.project.ProjectSupport;

public class CreateFlowProjectWizard extends Wizard implements INewWizard {
	IWorkbench workbench;
	IStructuredSelection selection;
	IProject project;
	IFolder flowFolder;
	WizardNewProjectCreationPage projectPage;

	public CreateFlowProjectWizard() {
		setWindowTitle("New Flow Project");
	}

	/**
	 * wizard可能有多个操作页面，当点击next时会调用getNextPage方法来返回下一个操作页面。
	 * 
	 */
	@Override
	public void addPages() {
		projectPage = new WizardNewProjectCreationPage("new flow project");
		projectPage.setTitle("new flow project");
		this.addPage(projectPage);
	}

	/**
	 * 是否禁用next和previous按钮
	 */
	@Override
	public boolean needsPreviousAndNextButtons() {
		return false;
	}

	/**
	 * 当点击finish按钮时，生成新建的文件
	 */
	@Override
	public boolean performFinish() {

		String projectName = projectPage.getProjectName();
		URI location = null;
		if (!projectPage.useDefaults()) {
			location = projectPage.getLocationURI();
		}
		ProjectSupport.createProject(projectName, location);
		
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

}
