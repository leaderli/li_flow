package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

import com.leaderli.li.flow.listener.NotifyListener;

@SuppressWarnings("rawtypes")
public class NodeNotify implements ModelRole {

	private transient List<NotifyListener> notifys = new ArrayList<>();

	public NodeNotify() {
		super();
	}

	public void addNotify(NotifyListener notify) {
		notifys.add(notify);
	}

	public void removeNotify(NotifyListener notify) {
		notifys.remove(notify);
	}

	public void notifyChanged() {
		this.notifyChanged(ALL_TYPE | ALL_ROLE);
	}

	public void notifyChanged(int typeRole) {
		this.notifyChanged(typeRole, "", "");
	}

	@SuppressWarnings("unchecked")
	public <T> void notifyChanged(int typeRole, T oldVal, T newVal) {
		notifys.stream()
				.filter(notify -> notify.canNotifyForModel(this) && ModelRole.isTypeRole(notify.typeAndRole(), typeRole))
				.forEach(notify -> {
					notify.notifyChanged(oldVal, newVal);
				});
	}

}