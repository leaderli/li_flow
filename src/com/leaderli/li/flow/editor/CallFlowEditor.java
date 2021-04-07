package com.leaderli.li.flow.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.image.TabImageProvider;
import com.leaderli.li.flow.listener.SelectionListenerIgnoreDefault;
import com.leaderli.li.flow.util.ImageUtil;

public class CallFlowEditor extends MultiPageEditorPart {
	private static final String EDITOR_PART_COMPOSITE_DATA_KEY = "IEditorPart";
	private int applicationFlowPage = 0;
	private List<IEditorPart> nestedControlEditors = new ArrayList<>();
	@Override
	protected void createPages() {
		try {
			this.applicationFlowPage = addCloseablePage(new FlowEditor(), false);
			this.setPageText(this.applicationFlowPage, "li flow");
			this.setActivePage(this.applicationFlowPage);
			IWorkbenchPage page = this.getEditor(applicationFlowPage).getSite().getPage();

		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}



	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		IWorkbenchPage page = this.getSite().getPage();
		IViewReference viewRef = page.findViewReference(PaletteView.ID);
		if (viewRef != null) {

			IViewPart paletteView = viewRef.getView(false);
			if (paletteView != null) {
				IPartListener listener = paletteView.getAdapter(IPartListener.class);
				if (listener != null) {
					listener.partClosed(this);
					listener.partActivated(this);
				}
			}
		}
	}

	@Override
	protected IEditorSite createSite(IEditorPart editor) {
		IEditorSite site = new MultiPageEditorSite(this, editor) {
			@Override
			protected void handleSelectionChanged(SelectionChangedEvent event) {
				ISelectionProvider parentProvider = getMultiPageEditor().getSite().getSelectionProvider();
				if (parentProvider instanceof MultiPageSelectionProvider) {
					SelectionChangedEvent newEvent = new SelectionChangedEvent(parentProvider, event.getSelection());
					MultiPageSelectionProvider prov = (MultiPageSelectionProvider) parentProvider;
					prov.fireSelectionChanged(newEvent);
					prov.firePostSelectionChanged(newEvent);
				}
			}
		};

		return site;
	}


	public void openServletFlowNodeItemEditorPage(FlowNode flowNode, boolean active) throws PartInitException {

		FlowNodeObejctTreeEditor editor = new FlowNodeObejctTreeEditor();
		editor.setFlowNode(flowNode);
		int index = this.addCloseablePage(editor, true);
		this.setPageText(index, flowNode.getName());
		if (active) {
			this.setActivePage(index);

		}
	}
	private int addCloseablePage(IEditorPart editor, boolean closeButtonVisible) throws PartInitException {

		while (this.getPageCount() > 1) {
			this.removePage(this.getPageCount() - 1);
		}
		IEditorSite site = this.createSite(editor);
		editor.init(site, this.getEditorInput());
		Composite topLevel = new Composite(this.getContainer(), SWT.NORMAL);
		topLevel.setLayout(new FillLayout());
		topLevel.setData(EDITOR_PART_COMPOSITE_DATA_KEY, editor);

		Composite closeableComposite = new Composite(topLevel, SWT.NORMAL);
		GridLayout closeableLayout = new GridLayout();
		closeableLayout.marginHeight = 0;
		closeableLayout.marginWidth = 0;
		closeableLayout.verticalSpacing = 0;
		closeableComposite.setLayout(closeableLayout);
		closeableComposite.setBackground(this.getContainer().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		GridData closeButtonData = new GridData(16, 16);
		closeButtonData.horizontalAlignment = SWT.RIGHT;

		Button closeButton = new Button(closeableComposite, SWT.FLAT | SWT.PUSH);
		closeButton.setImage(ImageUtil.getImage("closebutton.gif"));
		closeButton.setLayoutData(closeButtonData);
		closeButton.setVisible(closeButtonVisible);
		closeButton.addSelectionListener(new SelectionListenerIgnoreDefault() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int activePage = CallFlowEditor.this.getActivePage();
				int newActivePage = activePage - 1;
				doSave(null);
				CallFlowEditor.this.setActivePage(newActivePage);
				CallFlowEditor.this.pageChange(newActivePage);
				CallFlowEditor.this.removePage(activePage);
			}

		});

		Composite editorComposite = new Composite(closeableComposite, SWT.BORDER);
		editorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		editorComposite.setLayout(new FillLayout());
		editor.createPartControl(editorComposite);
		editor.addPropertyListener(new IPropertyListener() {

			@Override
			public void propertyChanged(Object source, int propId) {
				CallFlowEditor.this.handlePropertyChange(propId);
			}
		});

		int pageIndex = this.addPage(topLevel);

		TabImageProvider tabImageProvider = editor.getAdapter(TabImageProvider.class);
		editor.addPropertyListener(new IPropertyListener() {

			@Override
			public void propertyChanged(Object source, int propId) {
				CallFlowEditor.this.handlePropertyChange(propId);

			}

		});
		if (tabImageProvider != null) {
			this.setPageImage(pageIndex, tabImageProvider.getImage());
		}
		return pageIndex;
	}

	@Override
	public boolean isDirty() {
		for (IEditorPart editorPart : editorParts()) {
			if (editorPart.isDirty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

		for (IEditorPart editorPart : editorParts()) {
			editorPart.doSave(monitor);
		}
		firePropertyChange(PROP_DIRTY);
		firePropertyChange(PROP_TITLE);

	}

	private List<IEditorPart> editorParts() {
		List<IEditorPart> editorParts = new ArrayList<>();
		for (int i = 0; i < getPageCount(); i++) {
			IEditorPart editorPart = getEditor(i);
			if (editorPart != null) {
				editorParts.add(editorPart);
			}
		}
		return editorParts;
	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

		Assert.isTrue(editorInput instanceof IFileEditorInput, "Invalid Input: Must be IFileEditorInput");

		super.init(site, editorInput);
		this.setPartName(editorInput);
	}

	private void setPartName(IEditorInput editorInput) {
		// 获取打开的文件
		IFile inputFile = ((IFileEditorInput) editorInput).getFile();
		// 拼接 项目名和文件名
		String title = inputFile.getProject().getName() + "[" + inputFile.getName() + "]";
		this.setPartName(title);
	}





	@Override
	public int addPage(Control control) {
		if (control instanceof Composite) {
			IEditorPart part = (IEditorPart) control.getData(EDITOR_PART_COMPOSITE_DATA_KEY);
			if(part!=null) {
				this.nestedControlEditors.add(part);
			}
		}
		return super.addPage(control);
	}

	@Override
	public void removePage(int pageIndex) {
		IEditorPart part = this.getEditor(pageIndex);
		super.removePage(pageIndex);
		if (part != null) {
			this.nestedControlEditors.remove(part);
			this.disposePart(part);
		}
	}

	@Override
	protected IEditorPart getEditor(int pageIndex) {
		IEditorPart editor = super.getEditor(pageIndex);
		if (editor == null) {
			Control ctrl = this.getControl(pageIndex);
			if (ctrl instanceof Composite) {
				editor = (IEditorPart) ctrl.getData(EDITOR_PART_COMPOSITE_DATA_KEY);
			}
		}
		// System.out.println("editor: " + editor + " site:" + editor.getSite() + "
		// page:" + editor.getSite().getPage()
		// + " active:"
		// + editor.getSite().getPage().getActiveEditor());
		return editor;
	}

	@Override
	public void dispose() {
		this.nestedControlEditors.forEach(this::disposePart);
		super.dispose();
	}

	void disposePart(IWorkbenchPart part) {
		SafeRunner.run(new SafeRunnable() {

			@Override
			public void run() throws Exception {
				if (part.getSite() instanceof MultiPageEditorSite) {
					((MultiPageEditorSite) part.getSite()).dispose();
				}
				part.dispose();

			}
		});
	}


	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (this.getActiveEditor() != null) {
			return this.getActiveEditor().getAdapter(adapter);
		}

		return super.getAdapter(adapter);
	}

	// @Override
	// protected IEditorPart getActiveEditor() {
	// return this.nestedControlEditors.get(0);
	// // return super.getActiveEditor();
	// }

}
