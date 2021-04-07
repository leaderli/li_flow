package com.leaderli.li.flow.editor.command;
 
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.Location;
 
public class FlowNodeChangeConstraintCommand extends ModelCommand<FlowNode,FlowDiagram>{
 
	private Location oldPoint;
	private Location newPoint;
   
  @Override public void execute() {
    if(oldPoint== null ) {
    	this.oldPoint = model.getPoint();
    }
    
    model.setPoint( this.newPoint);
  }
 
  @Override public void undo() {
	  model.setPoint(this.oldPoint);
  }

 

   
  public void setNewPoint(Location point) {
	  this.newPoint = point;
  }
}