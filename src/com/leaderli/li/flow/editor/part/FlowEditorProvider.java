package com.leaderli.li.flow.editor.part;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;

public interface FlowEditorProvider<T extends BaseGraphicalEditorWithFlyoutPalette> {

	T getEditor();

	default void setEditor(T editor) {

	}
	




}
