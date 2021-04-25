package com.leaderli.li.flow.editor.model;

import org.eclipse.gef.EditPart;

public class Node<T extends NodeNotify> extends NodeNotify {
	private int id;
	private transient T parent;
	private transient EditPart editPart;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public T getParent() {
		return parent;
	}

	public void setParent(T parent) {
		this.parent = parent;
	}
//
//	@SuppressWarnings("unchecked")
//	public <R extends EditPart> R getEditPart() {
//		return (R) editPart;
//	}
//
//	public void setEditPart(EditPart editPart) {
//		this.editPart = editPart;
//	}
	
	

}
