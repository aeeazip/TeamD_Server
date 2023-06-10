package com.presenty.backend.error.exception;

public class EntityAccessDeniedException extends BusinessException {

    public EntityAccessDeniedException(String message) {
        super(ErrorCode.ENTITY_ACCESS_DENIED, message);
    }
}
