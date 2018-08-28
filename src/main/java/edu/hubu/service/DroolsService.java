package edu.hubu.service;

import edu.hubu.dto.MatchInvoiceExpenselRuleRequestDto;

public interface DroolsService {

    String fireRule();

    String countScore();

    String testAddScore();

    MatchInvoiceExpenselRuleRequestDto AddScore(MatchInvoiceExpenselRuleRequestDto o);
}
