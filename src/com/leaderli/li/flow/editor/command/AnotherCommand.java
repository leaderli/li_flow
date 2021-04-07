package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.model.Node;

public class AnotherCommand<P> extends Command{

}

class Mdel< T extends Node<?>,P> extends AnotherCommand<P> {
	 P p;
}
