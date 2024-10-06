package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Contact;
import com.deopraglabs.api_prysme.data.model.ContactType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class ContactInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long id;
    private ContactType contactType;
    private String value;
    private Contact contactName;
    private long contactId;
}
