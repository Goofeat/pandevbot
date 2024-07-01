package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryService;
import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AddChildCommand implements Command {

	private final SendMessageService sendMessageService;
	private final CategoryService categoryService;

	public AddChildCommand(SendMessageService sendMessageService, CategoryService categoryService) {
		this.sendMessageService = sendMessageService;
		this.categoryService = categoryService;
	}

	@Override
	public void execute(Update update) {
		String messageText = update.getMessage().getText();
		long userId = update.getMessage().getChat().getId();

		String[] parts = messageText.split(" ", 3);
		String response;
		if (parts.length == 3) {
			response = categoryService.addElement(userId, parts[1], parts[2]);
		} else {
			response = "Неверный формат команды.";
		}
		sendMessageService.sendMessage(update.getMessage().getChatId(), response);
	}
}
