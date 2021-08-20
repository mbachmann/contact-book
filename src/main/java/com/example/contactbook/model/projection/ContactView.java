package com.example.contactbook.model.projection;


import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Lob;

public interface ContactView {
    Long getId();
    @Schema(description = "The photo of the contact", type = "string", format = "binary")
    @Lob
    byte[] getPhoto();
    @Schema(description = "The thumbnail of the contact", type = "string", format = "binary")
    @Lob
    byte[] getThumbNail();
    @Schema(description = "The content type like image/jpeg or image/png of the thumbnail or photo")
    String getPhotoContentType();
    String getFirstName();
    String getMiddleName();
    String getLastName();
    String getCompany();
    LocalDate getBirthDate();
    String getNotes();
    String getAddressesAggregate();
    String getPhonesAggregate();
    String getEmailsAggregate();

}

