package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryService;
import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RemoveElementCommand implements Command {

	private final SendMessageService sendMessageService;
	private final CategoryService categoryService;

	public RemoveElementCommand(SendMessageService sendMessageService, CategoryService categoryService) {
		this.sendMessageService = sendMessageService;
		this.categoryService = categoryService;
	}

	@Override
	public void execute(Update update) {
		String messageText = update.getMessage().getText();
		long userId = update.getMessage().getChat().getId();

		String[] parts = messageText.split(" ", 2);
		String response;
		if (parts.length == 2) {
			response = categoryService.removeElement(userId, parts[1]);
		} else {
			response = "Неверный формат команды.";
		}
		sendMessageService.sendMessage(update.getMessage().getChatId(), response);
	}
}
