package com.sdu.pandevbot.service;

import org.telegram.telegrambots.meta.api.objects.InputFile;

public interface SendMessageService {
	void sendMessage(long chatId, String message);

	void sendDocument(long chatId, InputFile file, String caption);
}
