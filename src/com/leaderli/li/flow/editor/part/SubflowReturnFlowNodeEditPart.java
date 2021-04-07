package com.leaderli.li.flow.editor.part;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.adapter.Notify;
import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.ModelFieldEnum;
import com.leaderli.li.flow.util.GsonUtil;

public class SubflowReturnFlowNodeEditPart extends FlowNodeEditPart {
	@Override
	protected void initAfterSetModel() {
		super.initAfterSetModel();
		this.addNotify(new RenameSubflowReturnFlowNodeAdapter());
	}

	private class RenameSubflowReturnFlowNodeAdapter implements Notify {

		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			List<IFile> flows = LiPlugin.getDefault().getCallflowController().getProjectFlowFiles(SubflowReturnFlowNodeEditPart.this.getProject());
			flows.forEach(flow -> {

				try {
					FlowDiagram flowDiagram = GsonUtil.fromJson(flow.getContents(), FlowDiagram.class);
					flowDiagram.getFlowNodes().stream().filter(node -> PluginConstant.TYPE_SUBFLOW_REF.equals(node.getType())).forEach(node -> {
						List<GotoNode> temp = new ArrayList<>(node.getGotoNodes());
						temp.forEach(node::removeGotoNode);
					});
					InputStream stream = new ByteArrayInputStream(GsonUtil.toJson(flowDiagram).getBytes());
					flow.setContents(stream, true, true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}

			});
		}

	}
}
