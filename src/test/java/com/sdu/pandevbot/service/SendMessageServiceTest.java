package com.sdu.pandevbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@DisplayName("Unit-level testing for SendMessageService")
public class SendMessageServiceTest {

	private SendMessageService sendMessageService;
	private TelegramClient telegramClient;

	@BeforeEach
	public void init() {
		telegramClient = Mockito.mock(TelegramClient.class);
		sendMessageService = new SendMessageServiceImpl(telegramClient);
	}

	@Test
	public void shouldProperlySendMessage() throws TelegramApiException {
		//given
		long chatId = 123L;
		String text = "test_message";

		SendMessage message = SendMessage
				.builder()
				.chatId(chatId)
				.text(text)
				.parseMode("HTML")
				.build();

		//when
		sendMessageService.sendMessage(chatId, text);

		//then
		Mockito.verify(telegramClient).execute(message);
	}
}