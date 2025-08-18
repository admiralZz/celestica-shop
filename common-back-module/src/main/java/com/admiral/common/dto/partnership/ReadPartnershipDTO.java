package com.admiral.common.dto.partnership;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
// для десериализации DTO с Value и Builder в json. Например чтобы можно было юзать эти сущности в контроллерах
@Jacksonized
public class ReadPartnershipDTO {
    String firstName;
    String lastName;
    String company;
    String email;
    String phone;
    String location;
    String cooperationType;
    String productCategory;
    String message;
}
