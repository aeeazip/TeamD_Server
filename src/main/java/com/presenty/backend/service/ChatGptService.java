package com.presenty.backend.service;

import com.presenty.backend.domain.member.Mbti;
import com.presenty.backend.domain.member.Member;
import com.presenty.backend.domain.member.repository.MemberRepository;
import com.presenty.backend.error.exception.EntityNotFoundException;
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

    private final MemberRepository memberRepository;

    public ChatGptService(@Value("${chatgpt.key}") String apiKey, MemberRepository memberRepository) {
        this.service = new OpenAiService(apiKey);
        this.memberRepository = memberRepository;
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

    public List<String> recommendPaper(Long takerId) {
        Member taker = getMemberById(takerId);
        String prompt = createPaperPrompt(taker.getMbti());
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

    private String createPaperPrompt(Mbti mbti) {
        return mbti + " 친구가 좋아할 축하 메시지를 80자 이내로 3가지 만들어줘";
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member id = " + memberId));
    }
}
