package com.presenty.backend.error.exception;

public class DuplicateFollowingException extends BusinessException {
    public DuplicateFollowingException(String message) {
        super(ErrorCode.DUPLICATED_FOLLOWINGS, message);
    }
}
