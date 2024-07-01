package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommand implements Command {

	private final SendMessageService sendMessageService;

	public final static String START_MESSAGE = """
			Привет!
			
			Я бот для управления деревом категорий. Ты можешь создавать, просматривать и удалять категории с помощью команд.
			
			Используйте команду /help для получения списка доступных команд.
			""";

	public StartCommand(SendMessageService sendBotMessageService) {
		this.sendMessageService = sendBotMessageService;
	}

	@Override
	public void execute(Update update) {
		sendMessageService.sendMessage(update.getMessage().getChatId(), START_MESSAGE);
	}
}