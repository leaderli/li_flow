package com.leaderli.li.flow.editor.part;

import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;

import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.policy.FlowDiagramXYLayoutPolicy;
import com.leaderli.li.flow.listener.NotifyListener;

public class FlowDiagramEditPart extends GenericsEditPart<FlowDiagram, FlowEditor> {


	@Override
	protected IFigure createFigure() {
		// 定义一个基本的画布
		FreeformLayer layer = new FreeformLayer();
		layer.setBorder(new GroupBoxBorder("Diagram"));
		layer.setLayoutManager(new FreeformLayout());
		layer.setBorder(new MarginBorder(5));
		layer.setOpaque(false);
		return layer;
	}

	@Override
	protected void createEditPolicies() {
		// 关于布局的处理策略
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowDiagramXYLayoutPolicy());
	}

	@Override
	protected void initAfterSetModel() {
		addNotifyListener(new FlowDiagramModelChildrenNotifyListener());
	}

	@Override
	public List<FlowNode> getModelChildren() {
		return getModel().getFlowNodes();
	}

	/**
	 * 返回子Figure的父Figure，默认返回createFigure时创建的Figure, 该方法仅为编写注释而用，无实际用图
	 */
	@Override
	public IFigure getContentPane() {

		return super.getContentPane();
	}



	private class FlowDiagramModelChildrenNotifyListener implements NotifyListener<FlowNode> {

		/**
		 * 新增或删除FlowNode时，重新绘制画布
		 */
		@Override
		public void notifyChanged() {
			refreshChildren();
		}
	}

}