package com.presenty.backend.service;

import com.presenty.backend.service.dto.WishlistRecommendRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatGptService {

    private static final String MODEL = "gpt-3.5-turbo";

    private final OpenAiService service;

    public ChatGptService(@Value("${chatgpt.key}") String apiKey) {
        this.service = new OpenAiService(apiKey);
    }

    public List<String> recommendWishlist(WishlistRecommendRequest request) {
        String prompt = createWishlistPrompt(request);
        ChatCompletionRequest requester = ChatCompletionRequest.builder()
                .model(MODEL)
                .messages(List.of(
                        new ChatMessage("user", prompt)
                )).build();
        return Arrays.stream(service.createChatCompletion(requester).getChoices().stream()
                .map(ChatCompletionChoice::getMessage)
                .map(ChatMessage::getContent)
                .toList()
                .get(0)
                .split("\\d. "))
                .filter(wish -> !wish.isEmpty())
                .map(String::trim)
                .toList();
    }

    private String createWishlistPrompt(WishlistRecommendRequest request) {
        String interests = request.getInterests().stream()
                .map(Interest::getTitle)
                .collect(Collectors.joining(", "));
        String tags = request.getTags().stream()
                .map(Tag::getTitle)
                .collect(Collectors.joining(", "));
        String choice = request.getChoice().getTitle();
        return interests + "에 관심이 있는 사람들을 위한 " +
                tags + " 느낌의 " + choice + "선물 3가지만 단어로 추천해줘";
    }
}
