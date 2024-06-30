package com.sdu.pandevbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sdu.pandevbot.Constants.HELP_MESSAGE;
import static com.sdu.pandevbot.Constants.START_MESSAGE;

@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

	private final TelegramClient telegramClient;

	@Autowired
	private Service categoryService;

	public Bot() {
		telegramClient = new OkHttpTelegramClient(getBotToken());
	}

	@Override
	public String getBotToken() {
		return "7354608554:AAEPCbyRAtT5mCzGFDyK0KYU6N4GAgaSZq8";
	}

	@Override
	public LongPollingUpdateConsumer getUpdatesConsumer() {
		return this;
	}

	@Override
	public void consume(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			String messageText = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			String username = update.getMessage().getChat().getUserName();
			long userId = update.getMessage().getChat().getId();

			log(username, Long.toString(userId), messageText);

			if (messageText.equalsIgnoreCase("/start")) {
				sendStartMessage(chatId);
			} else if (messageText.equalsIgnoreCase("/viewtree") || messageText.equalsIgnoreCase("/vt")) {
				String tree = categoryService.viewTree();
				sendMessage(chatId, tree.isEmpty() ? "Дерево категорий пусто" : tree);
			} else if (messageText.startsWith("/addElement") || messageText.startsWith("/add")) {
				String[] parts = messageText.split(" ", 3);
				String response;
				if (parts.length == 2) {
					response = categoryService.addElement(parts[1]);
				} else if (parts.length == 3) {
					response = categoryService.addElement(parts[1], parts[2]);
				} else {
					response = "Неверный формат команды.";
				}
				sendMessage(chatId, response);
			} else if (messageText.startsWith("/removeElement") || messageText.startsWith("/rm")) {
				String[] parts = messageText.split(" ", 2);
				String response;
				if (parts.length == 2) {
					response = categoryService.removeElement(parts[1]);
				} else {
					response = "Неверный формат команды.";
				}
				sendMessage(chatId, response);
			} else if (messageText.equals("/help")) {
				sendHelpMessage(chatId);
			}
		}
	}

	private void sendStartMessage(long chatId) {
		sendMessage(chatId, START_MESSAGE);
	}

	private void sendHelpMessage(long chatId) {
		sendMessage(chatId, HELP_MESSAGE);
	}

	private void sendMessage(long chatId, String text) {
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

	@AfterBotRegistration
	public void afterRegistration(BotSession botSession) {
		System.out.println("Registered bot running state is: " + botSession.isRunning());
	}

	private void log(String username, String userId, String messageText) {
		System.out.println("\n ----------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("Message from @" + username + ". (id = " + userId + ") \n Text - " + messageText);
	}
}
