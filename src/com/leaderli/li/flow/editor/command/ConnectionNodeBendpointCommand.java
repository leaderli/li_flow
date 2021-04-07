package com.leaderli.li.flow.editor.command;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.requests.BendpointRequest;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.Location;

public class ConnectionNodeBendpointCommand extends ModelCommand<ConnectionNode,FlowDiagram> {

	protected int index;
	protected Location location;
	protected Connection connection;
	
	public static final int CLOSEST_INTERVAL = 10;

	public ConnectionNodeBendpointCommand(BendpointRequest request) {

		super();
		setModel((ConnectionNode) request.getSource().getModel());
		this.index = request.getIndex();
		this.connection = (((AbstractConnectionEditPart) request.getSource()).getConnectionFigure());
		this.location = Location.transfer(request.getLocation());

	}

	@Override
	public void execute() {
		reviseLocation();
	}

	private void reviseLocation() {

		System.out.println("--------------before-----------------" + this.getClass());
		System.out.println(
				"create index:" + index + " location:" + location + " size:" + this.connection.getPoints().size());
		Point prev = this.connection.getPoints().getPoint(index);
		Point next = this.connection.getPoints().getPoint(index + 2);

		Location newLocation = new Location(prev.x(), next.y());
		System.out.println("prev:" + prev + " next:" + next);
		System.out.println("newLocation:" + newLocation);
		System.out.println("--------------end before-----------------");
		int x = getClosetNum(prev.x(), next.x(), location.getX());
		int y = getClosetNum(prev.y(), next.y(), location.getY());
		this.location = new Location(x,y);

	}
	
	private int getClosetNum(int begin,int end ,int num ) {
		double interval = Math.abs(begin -end);
		double beginInterval = Math.abs(begin- num);
		if(beginInterval* 100 /interval< CLOSEST_INTERVAL) {
			return begin;
		}
		double endInterval= Math.abs(end- num);
		if(endInterval* 100 /interval< CLOSEST_INTERVAL) {
			return end;
		}
		return num;
	}

}