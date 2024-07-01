package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.SendMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class UnknownCommand implements Command {

	public static final String UNKNOWN_MESSAGE = "Не понимаю вас \uD83D\uDE1F, напишите /help чтобы узнать что я понимаю.";

	private final SendMessageService sendMessageService;

	public UnknownCommand(SendMessageService sendBotMessageService) {
		this.sendMessageService = sendBotMessageService;
	}

	@Override
	public void execute(Update update) {
		sendMessageService.sendMessage(update.getMessage().getChatId(), UNKNOWN_MESSAGE);
	}
}
