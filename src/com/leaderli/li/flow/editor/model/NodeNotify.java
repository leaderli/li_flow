package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

import com.leaderli.li.flow.adapter.Notify;

public class NodeNotify implements ModelRole {

	private transient List<Notify> notifys = new ArrayList<>();

	public NodeNotify() {
		super();
	}

	public void addNotify(Notify notify) {
		notifys.add(notify);
	}

	public void removeNotify(Notify notify) {
		notifys.remove(notify);
	}

	public void notifyChanged() {
		this.notifyChanged(ALL_TYPE | ALL_ROLE);
	}

	public void notifyChanged(int typeRole) {
		this.notifyChanged(typeRole, "", "");
	}
	public void notifyChanged(int typeRole, String oldVal, String newVal) {
		notifys.stream()
				.filter(notify -> notify.canNotifyForModel(this) && notify.canNotifyForTypeAndRole(typeRole))
				.forEach(notify -> {
					notify.notifyChanged(typeRole, oldVal, newVal);
				});
	}

}