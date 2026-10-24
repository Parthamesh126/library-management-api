package com.symphonyai.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "library")
public record LibraryProperties(
    int loanPeriodDays,
    int maxActiveBorrowsPerMember
) {}
