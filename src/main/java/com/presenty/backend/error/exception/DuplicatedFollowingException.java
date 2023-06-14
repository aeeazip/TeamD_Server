package com.presenty.backend.error.exception;

public class DuplicatedFollowingException extends BusinessException {
    public DuplicatedFollowingException(String message) {
        super(ErrorCode.DUPLICATED_FOLLOWINGS, message);
    }
}
