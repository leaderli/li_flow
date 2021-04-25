package com.leaderli.li.flow.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;

public abstract class BaseGraphicalEditorWithFlyoutPalette extends GraphicalEditorWithFlyoutPalette {

	protected IFile flowFile;

	/**
	 * 扩大访问权限
	 */
	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	public final void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

		Assert.isTrue(editorInput instanceof IFileEditorInput, "Invalid Input: Must be IFileEditorInput");
		flowFile = ((IFileEditorInput) editorInput).getFile();
		setPartName(flowFile);

		// 该方法会初始化palette
		setEditDomain(new DefaultEditDomain(this));
		super.init(site, editorInput);

	}

	// 编辑器的标题
	protected void setPartName(IFile inputFile) {

	}

	public final IFile getFile() {
		return flowFile;
	}

	public final IProject getProject() {
		return flowFile.getProject();
	}

}
