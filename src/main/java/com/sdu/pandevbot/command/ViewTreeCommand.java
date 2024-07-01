package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryServiceImpl;
import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ViewTreeCommand implements Command {

	private final SendMessageService sendMessageService;
	private final CategoryServiceImpl categoryService;

	public ViewTreeCommand(SendMessageService sendMessageService, CategoryServiceImpl categoryService) {
		this.sendMessageService = sendMessageService;
		this.categoryService = categoryService;
	}

	@Override
	public void execute(Update update) {
		long userId = update.getMessage().getChat().getId();

		String tree = categoryService.viewTree(userId);
		sendMessageService.sendMessage(update.getMessage().getChatId(), tree.isEmpty() ? "Дерево категорий пусто" : tree);
	}
}
