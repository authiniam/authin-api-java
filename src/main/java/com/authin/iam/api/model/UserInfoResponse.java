package com.authin.iam.api.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserInfoResponse {

    //region Private Fields
    private String sub;

    private String profile;

    private String email;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "preferred_name")
    private String preferredUsername;

    private String name;

    @JsonProperty(value = "given_name")
    private String givenName;

    @JsonProperty(value = "family_name")
    private String familyName;
    //endregion

    //region Constructors
    public UserInfoResponse(String sub, String profile, String email, String phoneNumber, String preferredUsername, String name, String givenName, String familyName) {
        this.sub = sub;
        this.profile = profile;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.preferredUsername = preferredUsername;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
    }
    //endregion
}
