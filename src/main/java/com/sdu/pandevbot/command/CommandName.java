package com.sdu.pandevbot.command;

import lombok.Getter;

@Getter
public enum CommandName {

	START("/start"),
	HELP("/help"),
	NO("nocommand"),
	VIEWTREE("/viewtree"),
	ADD("/add"),
	ADDCHILD("/addchild"),
	REMOVE("/remove"),
	DOWNLOAD("/download");

	private final String commandName;

	CommandName(String commandName) {
		this.commandName = commandName;
	}

}
