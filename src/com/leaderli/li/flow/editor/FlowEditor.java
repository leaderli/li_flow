package com.leaderli.li.flow.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.editor.action.AstJavaAction;
import com.leaderli.li.flow.editor.action.EditRelateJavaAction;
import com.leaderli.li.flow.editor.action.SelectionContextMenuAction;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.part.ConnectionNodeEditPart;
import com.leaderli.li.flow.editor.part.EditPartFactoryUtil;
import com.leaderli.li.flow.editor.part.FlowDiagramEditPart;
import com.leaderli.li.flow.editor.part.GotoNodeEditPart;
import com.leaderli.li.flow.image.TabImageProvider;
import com.leaderli.li.flow.util.GsonUtil;
import com.leaderli.li.flow.util.ModelUtil;

/**
 * 
 * 编辑器的入口
 *
 */
public class FlowEditor extends GraphicalEditorWithFlyoutPalette {

	private IFile flowFile;
	private FlowEditorPaletteRoot palette;

	public FlowEditor() {
	}

	/**
	 * 定义Palette工具栏组件
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		if (this.palette == null) {

			this.palette = new FlowEditorPaletteRoot(this.getProject());
		}
		return this.palette.getPaletteRoot();
	}

	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}

	/**
	 * 保存文件时的回调函数
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// /**
		// * 可以将未保存状态的*号移除
		// */
		this.getCommandStack().markSaveLocation();

		IFile iFile = ((IFileEditorInput) this.getEditorInput()).getFile();
		Object model = this.getGraphicalViewer().getContents().getModel();
		InputStream stream = new ByteArrayInputStream(GsonUtil.toJson(model).getBytes());
		try {
			iFile.setContents(stream, true, true, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 当使用编辑器编辑文件时执行相关命令，跟根据isDity判断是否需要将文件的标题上加*，表示该文件变动未保存
	 */
//	@Override
//	public void commandStackChanged(EventObject event) {
//		firePropertyChange(PROP_DIRTY);
//		super.commandStackChanged(event);
//	}


	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {

		Assert.isTrue(editorInput instanceof IFileEditorInput, "Invalid Input: Must be IFileEditorInput");
		this.flowFile = ((IFileEditorInput) editorInput).getFile();
		LiPlugin.getDefault().getCallflowController().init(this.flowFile.getProject());
		this.setEditDomain(new DefaultEditDomain(this));
		super.init(site, editorInput);
		// 编辑器的标题
		this.setPartName(editorInput);

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private void setPartName(IEditorInput editorInput) {
		// 获取打开的文件
		IFile inputFile = ((IFileEditorInput) editorInput).getFile();
		// 拼接 项目名和文件名
		String title = inputFile.getProject().getName() + "[" + inputFile.getName() + "]";
		this.setPartName(title);
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		/**
		 * 设定编辑器编辑的文件内容,FlowDiagram为一个model，setContents接收一个model时，
		 * 会根据EditPartFactory找到对应的EditPart来处理
		 */

		IFile iFile = ((IFileEditorInput) this.getEditorInput()).getFile();
		this.getGraphicalViewer().setContents(this.loadInputFile(iFile));
	}

	private FlowDiagram loadInputFile(IFile file) {

		try {
			final FlowDiagram flowDiagram = GsonUtil.fromJson(file.getContents(), FlowDiagram.class);
			flowDiagram.getFlowNodes().forEach(flowNode -> {
				flowNode.setParent(flowDiagram);
				flowDiagram.registerNode(flowNode);
				flowNode.getGotoNodes().forEach(gotoNode -> {
					gotoNode.setParent(flowNode);
					flowDiagram.registerNode(gotoNode);
				});
			});
			flowDiagram.getConnectionNodes().forEach(connectionNode -> {
				connectionNode.setParent(flowDiagram);
				flowDiagram.registerNode(connectionNode);
			});
			return flowDiagram;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public IFile getFile() {

		return this.flowFile;
	}

	public IProject getProject() {

		return this.getFile().getProject();
	}

	/**
	 * 扩大访问权限
	 */
	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	/**
	 * 扩大访问权限
	 */
	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	private EditPartFactory editPartFactory;

	private EditPartFactory getEditPartFactory() {
		if (this.editPartFactory == null) {
			this.editPartFactory = (part, model) -> {
				EditPart editPart = null;

				if (model instanceof ConnectionNode) {
					editPart = new ConnectionNodeEditPart();
				} else if (model instanceof FlowDiagram) {
					editPart = new FlowDiagramEditPart();
					((FlowDiagram) model).setEditor(FlowEditor.this);

				} else if (model instanceof GotoNode) {
					editPart = new GotoNodeEditPart();

				} else if (model instanceof FlowNode) {
					String type = ((FlowNode) model).getType();
					editPart = EditPartFactoryUtil.getFlowNodeEditPartByType(type);
				}

				ModelUtil.referToEachOther(model, editPart);

				return editPart;
			};

		}
		return this.editPartFactory;
	}

	@Override
	protected void configureGraphicalViewer() {

		super.configureGraphicalViewer();
		FlowEditor editor = this;
		GraphicalViewer graphicalViewer = this.getGraphicalViewer();
		graphicalViewer.setEditPartFactory(this.getEditPartFactory());
		graphicalViewer.addDropTargetListener(new TemplateTransferDropTargetListener(graphicalViewer));
		this.getEditDomain().getPaletteViewer()
				.addDragSourceListener(new TemplateTransferDragSourceListener(this.getEditDomain().getPaletteViewer()));
		this.configureKeyboardShortcuts();
		graphicalViewer.setContextMenu(new FLowEditorContextMenuProvider(this.getActionRegistry(), graphicalViewer));

	}

	private void configureKeyboardShortcuts() {
		this.getGraphicalViewer().setKeyHandler(new FlowEditorKeyHandler(this.getGraphicalViewer()));
	}

	@Override
	protected void createActions() {

		this.createSelectionContextMenuAction(EditRelateJavaAction.class);
		this.createSelectionContextMenuAction(AstJavaAction.class);
		// ActionRegistry registry = this.getActionRegistry();
		// IAction action = new FlowNodeSelectionAction(this);
		// registry.registerAction(action);
		// this.getSelectionActions().add(action.getId());
		super.createActions();
	}

	private void createSelectionContextMenuAction(Class<? extends SelectionContextMenuAction<?>> actionClass) {
		try {
			SelectionContextMenuAction<?> action = actionClass.newInstance();
			action.setWorkbenchPart(this);
			this.getActionRegistry().registerAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == TabImageProvider.class) {
			return TabImageProvider.instance("flow.gif");
		} else if (type == IProject.class) {
			return this.getProject();
		} else if (type == IFile.class) {
			return this.getFile();
		}

		return super.getAdapter(type);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IWorkbenchPartSite site = this.getSite();
		if (site != null) {
			IWorkbenchWindow window = site.getWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorPart editorPart = page.getActiveEditor();
					if (editorPart != null) {
						this.updateActions(this.getSelectionActions());
					}
				}
			}
		}
	}

	private final class FlowEditorKeyHandler extends GraphicalViewerKeyHandler implements Listener {
		private ToolEntry activeEntry;
		private boolean enabled;

		private FlowEditorKeyHandler(GraphicalViewer viewer) {
			super(viewer);
			this.enabled = true;
			viewer.getControl().addListener(31, this);
		}

		@Override
		public boolean keyPressed(KeyEvent event) {
			if (this.enabled && event.keyCode == FlowEditorPaletteRoot.CONNECTION_TOOL_ACTIVATE_KEY_CODE) {
				PaletteViewer pv = FlowEditor.this.getEditDomain().getPaletteViewer();
				this.activeEntry = pv.getActiveTool();
				pv.setActiveTool(FlowEditor.this.palette.getConnectionTool());
			}
			return super.keyPressed(event);
		}

		@Override
		public boolean keyReleased(KeyEvent event) {
			if (this.enabled && event.keyCode == FlowEditorPaletteRoot.CONNECTION_TOOL_ACTIVATE_KEY_CODE) {
				FlowEditor.this.getEditDomain().getPaletteViewer().setActiveTool(this.activeEntry);
			}
			return super.keyReleased(event);
		}

		@Override
		public void handleEvent(Event e) {
		}

	}

}