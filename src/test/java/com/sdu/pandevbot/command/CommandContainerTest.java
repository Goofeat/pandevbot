package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryService;
import com.sdu.pandevbot.service.SendMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

@DisplayName("Unit-level testing for CommandContainer")
class CommandContainerTest {

	private CommandContainer commandContainer;

	@BeforeEach
	public void init() {
		SendMessageService sendMessageService = Mockito.mock(SendMessageService.class);
		CategoryService categoryService = Mockito.mock(CategoryService.class);
		commandContainer = new CommandContainer(sendMessageService, categoryService);
	}

	@Test
	public void shouldGetAllTheExistingCommands() {
		//when-then
		Arrays.stream(CommandName.values())
				.forEach(commandName -> {
					Command command = commandContainer.retrieveCommand(commandName.getCommandName());
					Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
				});
	}

	@Test
	public void shouldReturnUnknownCommand() {
		//given
		String unknownCommand = "/asdaded";

		//when
		Command command = commandContainer.retrieveCommand(unknownCommand);

		//then
		Assertions.assertEquals(UnknownCommand.class, command.getClass());
	}
}
