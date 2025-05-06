package org.example.JobSearch.service.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {
    private Map<String, Object> attributes;

    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }

    public String getName() {
        return (String) attributes.get("given_name");
    }

    public String getSurname() {
        return (String) attributes.get("family_name");
    }

    public String getPicture() {
        return (String) attributes.get("picture");
    }
}