package com.sdu.pandevbot.command;

import lombok.Getter;

@Getter
public enum CommandName {

	START("/start"),
	VIEWTREE("/viewtree"),
	ADD("/add"),
	ADDCHILD("/addchild"),
	REMOVE("/remove"),
	HELP("/help"),
	NO("nocommand");

	private final String commandName;

	CommandName(String commandName) {
		this.commandName = commandName;
	}

}
