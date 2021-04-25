package com.leaderli.li.flow.editor.part;

import com.leaderli.li.flow.listener.DefaultTypeNotifyListener;

public class SubflowReturnFlowNodeEditPart extends FlowNodeEditPart {
	@Override
	protected void initAfterSetModel() {
		super.initAfterSetModel();
		this.addNotifyListener(new RenameSubflowReturnFlowNodeAdapter());
	}

	private class RenameSubflowReturnFlowNodeAdapter implements DefaultTypeNotifyListener {

		@Override
		public void notifyChanged() {
//			List<IFile> flows = LiPlugin.getDefault().getCallflowController().getProjectFlowFiles(getProject());
//			flows.forEach(flow -> {
//
//				try {
//					SerializeFlowDiagram flowDiagram = GsonUtil.fromJson(flow.getContents(), SerializeFlowDiagram.class);
//					flowDiagram.getFlowNodes().stream().filter(node -> PluginConstant.TYPE_SUBFLOW_REF.equals(node.getType() && node.getFlowName())).forEach(node -> {
//						List<GotoNode> temp = new ArrayList<>(node.getGotoNodes());
//						temp.forEach(node::removeGotoNode);
//					});
//					InputStream stream = new ByteArrayInputStream(GsonUtil.toJson(flowDiagram).getBytes());
//					flow.setContents(stream, true, true, null);
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
//
//			});
		}

	}
}
