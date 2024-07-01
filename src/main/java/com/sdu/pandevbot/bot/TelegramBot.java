package com.sdu.pandevbot.bot;

import com.sdu.pandevbot.command.CommandContainer;
import com.sdu.pandevbot.service.CategoryService;
import com.sdu.pandevbot.service.SendMessageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sdu.pandevbot.command.CommandName.NO;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

	private final String COMMAND_PREFIX = "/";

	private final CommandContainer commandContainer;

	public TelegramBot(TelegramClient telegramClient, CategoryService categoryService) {
		this.commandContainer = new CommandContainer(
				new SendMessageServiceImpl(telegramClient),
				categoryService
		);
	}

	@Value("${bot.token}")
	private String token;

	@Override
	public String getBotToken() {
		return token;
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	@Override
	public void consume(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			String firstName = update.getMessage().getChat().getFirstName();
			String lastName = update.getMessage().getChat().getLastName();
			long userId = update.getMessage().getChat().getId();

			if (messageText.startsWith(COMMAND_PREFIX)) {
				String commandIdentifier = messageText.split(" ")[0].toLowerCase();

				commandContainer.retrieveCommand(commandIdentifier).execute(update);
			} else {
				commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
			}

			log(firstName, lastName, Long.toString(userId), messageText);
		}
	}

	@AfterBotRegistration
	public void afterRegistration(BotSession botSession) {
		System.out.println("Registered bot running state is: " + botSession.isRunning());
	}

	private void log(String firstName, String lastName, String userId, String messageText) {
		System.out.println("\n ----------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("Message from " + firstName + " " + lastName + ". (id = " + userId + ") \n Text - " + messageText);
	}
}
