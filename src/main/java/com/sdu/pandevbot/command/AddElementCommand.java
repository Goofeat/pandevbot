package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryServiceImpl;
import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AddElementCommand implements Command {

	private final SendMessageService sendMessageService;
	private final CategoryServiceImpl categoryService;

	public AddElementCommand(SendMessageService sendMessageService, CategoryServiceImpl categoryService) {
		this.sendMessageService = sendMessageService;
		this.categoryService = categoryService;
	}

	@Override
	public void execute(Update update) {
		String messageText = update.getMessage().getText();
		long userId = update.getMessage().getChat().getId();

		String[] parts = messageText.split(" ", 3);
		String response;
		if (parts.length == 2) {
			response = categoryService.addElement(userId, parts[1]);
		} else {
			response = "Неверный формат команды.";
		}
		sendMessageService.sendMessage(update.getMessage().getChatId(), response);
	}
}
