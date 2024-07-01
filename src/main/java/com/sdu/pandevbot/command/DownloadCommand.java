package com.sdu.pandevbot.command;

import com.sdu.pandevbot.service.CategoryServiceImpl;
import com.sdu.pandevbot.service.SendMessageService;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Log4j2
public class DownloadCommand implements Command {

	private final SendMessageService sendMessageService;
	private final CategoryServiceImpl categoryService;

	public DownloadCommand(SendMessageService sendMessageService, CategoryServiceImpl categoryService) {
		this.sendMessageService = sendMessageService;
		this.categoryService = categoryService;
	}

	@Override
	public void execute(Update update) {
		long chatId = update.getMessage().getChatId();
		long userId = update.getMessage().getChat().getId();

		try {
			String tree = categoryService.viewTree(userId);

			if (tree.isEmpty()) {
				sendMessageService.sendMessage(chatId, "Дерево категорий пусто");
			} else {
				byte[] fileContent = categoryService.downloadTree(userId);
				InputFile inputFile = new InputFile(new ByteArrayInputStream(fileContent), "CategoryTree.xlsx");

				sendMessageService.sendDocument(chatId, inputFile, "Файл готов!");
			}
		} catch (IOException e) {
			log.info(e);
			sendMessageService.sendMessage(chatId, "Произошла ошибка при создании файла.");
		} catch (Exception e) {
			log.info(e);
			sendMessageService.sendMessage(chatId, "Произошла ошибка при отправке файла.");
		}
	}
}
