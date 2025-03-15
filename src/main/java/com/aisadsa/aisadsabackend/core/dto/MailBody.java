package com.aisadsa.aisadsabackend.core.dto;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
