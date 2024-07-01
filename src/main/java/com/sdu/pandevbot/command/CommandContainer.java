package com.sdu.pandevbot.command;

import com.google.common.collect.ImmutableMap;
import com.sdu.pandevbot.service.CategoryService;
import com.sdu.pandevbot.service.SendMessageService;

import static com.sdu.pandevbot.command.CommandName.*;

public class CommandContainer {

	private final ImmutableMap<String, Command> commandMap;
	private final Command unknownCommand;

	public CommandContainer(SendMessageService sendMessageService, CategoryService categoryService) {

		commandMap = ImmutableMap.<String, Command>builder()
				.put(START.getCommandName(), new StartCommand(sendMessageService))
				.put(HELP.getCommandName(), new HelpCommand(sendMessageService))
				.put(NO.getCommandName(), new NoCommand(sendMessageService))
				.put(VIEWTREE.getCommandName(), new ViewTreeCommand(sendMessageService, categoryService))
				.put(ADD.getCommandName(), new AddElementCommand(sendMessageService, categoryService))
				.put(ADDCHILD.getCommandName(), new AddChildCommand(sendMessageService, categoryService))
				.put(REMOVE.getCommandName(), new RemoveElementCommand(sendMessageService, categoryService))
				.build();

		unknownCommand = new UnknownCommand(sendMessageService);
	}

	public Command retrieveCommand(String commandIdentifier) {
		return commandMap.getOrDefault(commandIdentifier, unknownCommand);
	}

}
