package com.admiral.common.dto.partnership;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
// для десериализации DTO с Value и Builder в json. Например чтобы можно было юзать эти сущности в контроллерах
@Jacksonized
public class CreatePartnershipDTO {
    @NotNull(message = "Fist name is required")
    String firstName;
    @NotNull(message = "Last name is required")
    String lastName;
    @NotNull(message = "Company name is required")
    String company;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;
    String phone;
    String location;
    @NotNull(message = "Coopertion type is required")
    String cooperationType;
    String productCategory;
    String message;
}
