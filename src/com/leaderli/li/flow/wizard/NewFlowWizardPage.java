package com.leaderli.li.flow.wizard;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.util.NameUtil;
import com.leaderli.li.flow.util.ResourcesUtil;

class NewFlowWizardPage extends WizardPage {
	/**
	 * 
	 */
	private final CreateFlowWizard Resources;

	public static final String PAGE_NAME = "NewFlowWizardPage";

	Text flowName;

	/**
	 * 校验当前页面是否操作完毕，进行下一步操作
	 */
	private void checkComplete() {

		setPageComplete(false);
		String fileName = flowName.getText();

		if (fileName.length() == 0) {
			this.setMessage("A flow name cannot be empty", IMessageProvider.WARNING);
			return;
		}

		IStatus validateJavaTypeName = NameUtil.validateJavaTypeName(fileName);
		if (!validateJavaTypeName.isOK()) {
			this.setMessage(validateJavaTypeName.getMessage(), validateJavaTypeName.getSeverity());
			return;
		}

		List<IFile> flowFiles = LiPlugin.getDefault().getCallflowController().getProjectFlowFiles(Resources.project);
		if (flowFiles.stream().map(ResourcesUtil::getFileSimpleName).anyMatch(f -> f.equalsIgnoreCase(fileName))) {
			this.setMessage("A flow named " + fileName + " already exists", IMessageProvider.ERROR);
			return;
		}

		this.setMessage("Create a new flow named " + fileName);
		setPageComplete(true);

	}

	protected NewFlowWizardPage(CreateFlowWizard createFlowWizard) {
		super(PAGE_NAME, "Create a new flow", null);
		Resources = createFlowWizard;
	}

	/**
	 * 绘制操作页面
	 */

	@Override
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NORMAL);
		setControl(composite);
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NORMAL).setText("Name:");
		flowName = new Text(composite, SWT.SINGLE | SWT.BORDER);
		flowName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		flowName.addModifyListener(e -> {
			checkComplete();
		});
		checkComplete();

	}

}