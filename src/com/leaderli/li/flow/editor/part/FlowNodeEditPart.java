package com.leaderli.li.flow.editor.part;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.adapter.Notify;
import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.CallFlowEditor;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.Location;
import com.leaderli.li.flow.editor.model.ModelRole;
import com.leaderli.li.flow.editor.policy.FlowNodeComponentEditPolicy;
import com.leaderli.li.flow.editor.policy.FlowNodeGraphicalNodeEditPolicy;
import com.leaderli.li.flow.generate.GenerateFromTemplate;
import com.leaderli.li.flow.generate.GenerateFromTemplate.Builder;
import com.leaderli.li.flow.generate.ModifyJava;
import com.leaderli.li.flow.ui.GenericsPropertySource;
import com.leaderli.li.flow.ui.ModelPropertyDescriptor;
import com.leaderli.li.flow.util.ExtendedChopboxAnchor;
import com.leaderli.li.flow.util.GEFUtil;
import com.leaderli.li.flow.util.ImageUtil;
import com.leaderli.li.flow.util.ModelUtil;
import com.leaderli.li.flow.util.ResourcesUtil;

public class FlowNodeEditPart extends GenericsEditPart<FlowNode> implements NodeEditPart, IMultiPageEditPart, ModelRole {

	public static final String FLOWNODE_OPEN_ROLE = "flowNodeOpen";

	private FlowNodeFigure figure;
	private IFile javaFile;
	private GenericsPropertySource<FlowNode> propertySource;

	@Override
	public FlowNodeFigure getFigure() {
		if (figure == null) {
			figure = new FlowNodeFigure();
		}
		return figure;
	}

	@Override
	protected IFigure createFigure() {
		return getFigure();
	}

	@Override
	protected List<GotoNode> getModelChildren() {
		return getModel().getGotoNodes();
	}

	/**
	 * getModelChildren的模型对应的editPart中的figure的父figure对象。这里指定gotoNode图形的位置
	 */
	@Override
	public IFigure getContentPane() {
		return getFigure().getContentFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new FlowNodeGraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new FlowNodeComponentEditPolicy());
	}

	@Override
	public void performRequest(Request req) {

		if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			FlowNode flowNode = getModel();
			ModelUtil.openRenameFLowNodeWizard(flowNode, getViewer().getEditDomain().getCommandStack());
			return;
		}
		executeCommand(getCommand(req));
	}

	protected void executeCommand(Command command) {

		if (command != null && command.canExecute()) {

			if (command.canUndo()) {
				getViewer().getEditDomain().getCommandStack().execute(command);
			} else {
				command.execute();
			}
		}
	}

	/**
	 * 更新flowNode的坐标，以及名称
	 */
	@Override
	protected void refreshVisuals() {
		Figure figure = getFigure();
		FlowNode model = getModel();
		getFigure().nodeLabel.setText(model.getName());
		AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart) getParent();
		Location point = model.getPoint();
		parent.setLayoutConstraint(this, figure, new Rectangle(point.getX(), point.getY(), -1, -1));
		super.refreshVisuals();
	}

	/**
	 * 线条的source端点落在gotoNode上
	 */
	@Override
	protected List<ConnectionNode> getModelSourceConnections() {
		return null;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	/**
	 * 线条的target端落在inputTerminal图形上，该图形没有model，没有editpart，因此直接在当前editpart去处理
	 */
	@Override
	protected List<ConnectionNode> getModelTargetConnections() {
		List<ConnectionNode> connectionNodes = ((FlowDiagramEditPart) getParent()).getModel().getConnectionNodes();
		return connectionNodes.stream().filter(connectionNode -> getModel().getId() == connectionNode.getTargetID())
				.collect(Collectors.toList());
	}

	/**
	 * 线条target端点的样式，需指定figure
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ExtendedChopboxAnchor(getFigure().getInputFigure(), PluginConstant.CHOPBOX_ANCHOR_LEFT);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ExtendedChopboxAnchor(getFigure().getInputFigure(), PluginConstant.CHOPBOX_ANCHOR_LEFT);
	}

	@Override
	public CallFlowEditor getMultiPageEditor() {
		DefaultEditDomain domain = (DefaultEditDomain) getViewer().getEditDomain();
		IEditorSite site = domain.getEditorPart().getEditorSite();
		if (site instanceof MultiPageEditorSite) {
			return (CallFlowEditor) ((MultiPageEditorSite) site).getMultiPageEditor();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			return propertySource;
		}
		if (adapter == IFile.class) {
			return getJavaFile();

		}
		return super.getAdapter(adapter);
	}

	private String generateNewSourceCode() {

		Builder build = GenerateFromTemplate.template(getModel().getType());
		build.addPlaceHolder("package", "package " + getModel().getParent().getPackageName() + ";");
		build.addPlaceHolder("name", getModel().getName());
		generateNewSourceCodeBody(build);
		return build.build(getProject());

	}

	protected void generateNewSourceCodeBody(Builder build) {

	}

	private void modifyJava() throws Exception {

		IFile file = getJavaFile();
		ModifyJava modifyJava = new ModifyJava();
		modifyJava.begin(file);
		modifyJava.modify(getModifyJavaPolicy());
		modifyJava.commit();

	}

	protected ASTVisitor getModifyJavaPolicy() {
		return new BranchesMethodASTVisitor(getModel());
	}

	protected IFile getJavaFile() {

		if (javaFile == null || !javaFile.exists()) {

			IProject iproject = getProject();
			FlowNode flowNode = getModel();

			String packageName = flowNode.getParent().getPackageName();
			IFolder sourceFolderOfSubflow = ResourcesUtil.getSourceFolderOfSubflow(iproject, packageName);
			GEFUtil.createFolderRecursion(sourceFolderOfSubflow);

			javaFile = sourceFolderOfSubflow.getFile(flowNode.getName() + ".java");
			if (!javaFile.exists()) {
				try {
					String sourceCode = generateNewSourceCode();
					javaFile.create(new ByteArrayInputStream(sourceCode.getBytes()), false, null);
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return javaFile;
	}

	protected void addPropertyDescriptor() {
		this.addPropertyDescriptor(new FlowNodeNameTextPropertyDescriptor());
	}

	protected void addPropertyDescriptor(ModelPropertyDescriptor<FlowNode, ?> wrapper) {
		propertySource.addModelPropertyDescriptor(wrapper);
	}

	public String[] getSubFlowNames() {

		List<String> subFlowNames = new ArrayList<>(
				LiPlugin.getDefault().getCallflowController().getSubFlowNames(getProject()));
		subFlowNames.remove(ResourcesUtil.getSimpleName(getModel().getParent().getEditor().getFile()));
		return subFlowNames.stream().toArray(len -> new String[len]);
	}

	@Override
	protected void initAfterSetModel() {
		this.addNotify(new RenameFlowNodeAdapter());
		this.addNotify(new MoveFlowNodeAdapter());
		this.addNotify(new ConnectionTargetFlowNodeAdapter());
		this.addNotify(new ModifyFlowNodeChildAdapter());
		this.addNotify(new ModifyJavaAdapter());

		propertySource = new GenericsPropertySource<>();

		this.addPropertyDescriptor();

	}

	private class FlowNodeNameTextPropertyDescriptor extends ModelPropertyDescriptor<FlowNode, String> {

		public FlowNodeNameTextPropertyDescriptor() {
			super(new TextPropertyDescriptor("", "Name"), getModel());
		}

		@Override
		public void setProperty(String newName) {

			if (StringUtils.equals(model.getType(), PluginConstant.TYPE_SUBFLOW_ENTRY)) {
				return;
			}
			ModelUtil.openRenameFLowNodeWizard(model, model.getParent().getEditor().getCommandStack(), newName);
		}

		@Override
		public String getProperty() {
			return model.getName();
		}

	}

	private class RenameFlowNodeAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {

			IFile java = getJavaFile();
			Assert.isNotNull(java, "");
			javaFile = null;

			ResourcesUtil.renameJavaFile(java, getModel().getName());

			refreshVisuals();
		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | NAME_ROLE;
		}
	}

	private class MoveFlowNodeAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			refreshVisuals();

		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | POINT_ROLE;
		}
	}

	private class ConnectionTargetFlowNodeAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			refreshTargetConnections();
		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | CONNECTION_TARGET_ROLE;
		}

	}

	private class ModifyFlowNodeChildAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			refreshChildren();
			try {
				modifyJava();
			} catch (Exception e) {
				e.printStackTrace();
			}

			getMultiPageEditor().doSave(null);
		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | CHILD_ROLE;
		}

	}

	private class ModifyJavaAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			try {
				modifyJava();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | JAVA_CODE_ROLE;
		}

	}

	private class FlowNodeFigure extends Figure {

		/**
		 * 左边箭头
		 */
		private IFigure inputTerminal;
		/**
		 * 图元名称
		 */
		private Label nodeLabel;
		/**
		 * 图元图标
		 */
		private IFigure nodeIcon;
		/**
		 * 右边图元出口作为一个复合组件，使其可以动态的删减出口
		 */
		private IFigure contentPane;
		/**
		 * 图元名称和图元图标组合一起的父figure
		 */
		private Figure imglabel;

		public FlowNodeFigure() {
			init();
		}

		private IFigure getInputFigure() {

			if (inputTerminal == null) {
				inputTerminal = createInputFigure();
			}
			return inputTerminal;
		}

		private IFigure createInputFigure() {

			Assert.isTrue(inputTerminal == null);
			inputTerminal = new Label(ImageUtil.getImage("inputleft.gif"));
			return inputTerminal;
		}

		public IFigure getContentFigure() {

			if (contentPane == null) {
				contentPane = new Figure();
				FlowLayout layout = new FlowLayout(false);
				layout.setMinorAlignment(OrderedLayout.ALIGN_TOPLEFT);
				contentPane.setLayoutManager(layout);
			}
			return contentPane;
		}

		public void init() {

			FlowLayout layout;

			IFigure input = getInputFigure();

			nodeIcon = new Label(ImageUtil.getImage(getModel().getIcon()));

			nodeLabel = new Label(getModel().getName());

			Figure images = new Figure();
			layout = new FlowLayout(true);
			layout.setMinorAlignment(0);
			layout.setMinorSpacing(1);

			images.setLayoutManager(layout);
			if (input != null) {
				images.add(input);
			}
			images.add(nodeIcon);
			images.add(new Label(ImageUtil.getImage("breakpointempty.gif")));

			imglabel = new Figure();
			layout = new FlowLayout(false);
			layout.setMajorAlignment(0);
			layout.setMinorSpacing(0);
			imglabel.setLayoutManager(layout);

			imglabel.add(images);
			imglabel.add(nodeLabel);

			contentPane = getContentFigure();

			setLayoutManager(new FlowLayout(true));
			this.add(imglabel);
			this.add(contentPane);
		}

	}

}