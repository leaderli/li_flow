package com.leaderli.li.flow.listener;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public interface SelectionListenerIgnoreDefault extends SelectionListener {

	@Override
	default void widgetDefaultSelected(SelectionEvent e) {

	}

}
