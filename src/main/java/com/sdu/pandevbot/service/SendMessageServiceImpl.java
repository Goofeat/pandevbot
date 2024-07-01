package com.sdu.pandevbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class SendMessageServiceImpl implements SendMessageService {

	private final TelegramClient telegramClient;

	@Autowired
	public SendMessageServiceImpl(TelegramClient telegramClient) {
		this.telegramClient = telegramClient;
	}

	@Override
	public void sendMessage(long chatId, String text) {
		if (isBlank(text)) return;

		SendMessage message = SendMessage
				.builder()
				.chatId(String.valueOf(chatId))
				.text(text)
				.parseMode("HTML")
				.build();

		try {
			telegramClient.execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}
