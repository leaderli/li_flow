package com.leaderli.li.flow.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.policy.ConnectionNodeEditPolicy;
import com.leaderli.li.flow.editor.policy.ConnectionNodeSelectionEditPolicy;

public class ConnectionNodeEditPart extends AbstractConnectionEditPart implements FlowEditorProvider<FlowEditor> {

	protected FlowEditor editor;


	@Override
	public FlowEditor getEditor() {
		return editor;
	}

	@Override
	public void setEditor(FlowEditor flowEditor) {
		this.editor = flowEditor;
	}
	public ConnectionNodeEditPart() {
		super();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionNodeEditPolicy());
		// TODO 线条生成策略后续优化
		// installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new
		// ConnectionNodeBendpointEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new ConnectionNodeSelectionEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		ConnectionNode connectionNode = (ConnectionNode) getModel();
		PolylineConnection conn = (PolylineConnection) super.createFigure();
		// conn.setConnectionRouter(new OrthogonalConnectionRouter());
		// conn.setConnectionRouter(new BendpointConnectionRouter());
		conn.setConnectionRouter(new ManhattanConnectionRouter());
		conn.setTargetDecoration(new PolylineDecoration());
		return conn;
	}


}