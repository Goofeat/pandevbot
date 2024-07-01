package com.sdu.pandevbot.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Log4j2
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
			log.info(e);
		}
	}

	@Override
	public void sendDocument(long chatId, InputFile file, String caption) {
		if (isBlank(caption)) return;

		SendDocument message = SendDocument
				.builder()
				.document(file)
				.chatId(chatId)
				.caption(caption)
				.parseMode("HTML")
				.build();

		try {
			telegramClient.execute(message);
		} catch (TelegramApiException e) {
			log.info(e);
		}
	}

}
