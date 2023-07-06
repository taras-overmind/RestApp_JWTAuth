package com.taras_overmind.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {
    private String name;
    private Set<String> emails = new HashSet<>();
    private Set<String> phoneNumbers = new HashSet<>();
}
