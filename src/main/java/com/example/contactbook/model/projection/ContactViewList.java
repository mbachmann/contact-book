package com.example.contactbook.model.projection;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.persistence.Lob;
import java.time.LocalDate;

public interface ContactViewList {
    // @Value("#{target.id}")
    @Schema(description = "The id of the contact")
    Long getId();
    @Schema(description = "The first name of the contact")
    String getFirstName();
    @Schema(description = "The last or surname name of the contact")
    String getLastName();
    @Schema(description = "The company of the contact")
    String getCompany();
    @Schema(description = "Aggregated information: company name, lastname, firstname", type = "string")
    String getName();
    @Schema(description = "The birth date of the contact", type = "string", format = "date")
    LocalDate getBirthDate();
    @Schema(description = "The thumbnail of the contact", type = "string", format = "binary")
    @Lob
    byte[] getThumbNail();
    @Schema(description = "The content type like image/jpeg or image/png of the thumbnail or photo", type = "string")
    String getPhotoContentType();
    @Schema(description = "Aggregated email adresses", type = "string")
    String getAddressesAggregate();
    @Schema(description = "Aggregated phone numbers", type = "string")
    String getPhonesAggregate();
    @Schema(description = "Aggregated email addresses", type = "string")
    String getEmailsAggregate();
    @Schema(description = "Aggregated group id's", type = "string")
    String getGroupsIdAggregate();
    @Schema(description = "Aggregated relations id's", type = "string")
    String getRelationsIdAggregate();
}

