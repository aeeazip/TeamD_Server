package com.presenty.backend.controller;

import com.presenty.backend.service.MemberService;
import com.presenty.backend.service.dto.CreateResult;
import com.presenty.backend.service.dto.MemberCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public CreateResult<String> signup(@RequestBody @Validated MemberCreateRequest request) {
        return memberService.createMember(request);
    }
}
