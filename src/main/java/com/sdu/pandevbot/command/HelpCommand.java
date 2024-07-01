package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.sdu.pandevbot.command.CommandName.*;

public class HelpCommand implements Command {

	private final SendMessageService sendMessageService;

	public final static String HELP_MESSAGE = String.format("""
			Доступные команды:
			
			%s – Показать дерево категорий
			%s – Добавить корневой элемент
			%s – Добавить дочерний элемент
			%s – Удалить элемент
			%s – Скачать Excel документ с деревом категорий
			%s – Показать это сообщение""",
			VIEWTREE.getCommandName(),
			ADD.getCommandName(),
			ADDCHILD.getCommandName(),
			REMOVE.getCommandName(),
			DOWNLOAD.getCommandName(),
			HELP.getCommandName());

	public HelpCommand(SendMessageService sendBotMessageService) {
		this.sendMessageService = sendBotMessageService;
	}

	@Override
	public void execute(Update update) {
		sendMessageService.sendMessage(update.getMessage().getChatId(), HELP_MESSAGE);
	}
}
