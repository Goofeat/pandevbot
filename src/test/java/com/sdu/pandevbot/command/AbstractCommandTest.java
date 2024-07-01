package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryServiceImpl;
import com.sdu.pandevbot.service.SendMessageService;
import com.sdu.pandevbot.service.SendMessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

abstract class AbstractCommandTest {

	protected TelegramClient telegramClient = Mockito.mock(TelegramClient.class);
	protected SendMessageService sendMessageService = new SendMessageServiceImpl(telegramClient);
	protected CategoryServiceImpl categoryService = Mockito.mock(CategoryServiceImpl.class);

	abstract String getCommandName();

	abstract String getCommandMessage();

	abstract Command getCommand();

	@Test
	public void shouldProperlyExecuteCommand() throws TelegramApiException {
		//given
		Long chatId = 1234567824356L;

		Update update = prepareUpdate(chatId, getCommandName());

		SendMessage message = SendMessage
				.builder()
				.chatId(chatId)
				.text(getCommandMessage())
				.parseMode("HTML")
				.build();

		//when
		getCommand().execute(update);

		//then
		Mockito.verify(telegramClient).execute(message);
	}

	public static Update prepareUpdate(Long chatId, String commandName) {
		Update update = new Update();
		Message message = Mockito.mock(Message.class);
		Mockito.when(message.getChatId()).thenReturn(chatId);
		Mockito.when(message.getText()).thenReturn(commandName);
		update.setMessage(message);
		return update;
	}
}
