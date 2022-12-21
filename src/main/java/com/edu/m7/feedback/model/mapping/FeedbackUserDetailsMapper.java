package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.entity.Account;
import com.edu.m7.feedback.security.FeedbackUserDetails;
import org.mapstruct.Mapper;

@Mapper
public interface FeedbackUserDetailsMapper {

    FeedbackUserDetails accountToUserDetails(Account account);
    Account userDetailsToAccount(FeedbackUserDetails userDetails);
}
