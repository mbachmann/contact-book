package com.example.contactbook.model.projection;


import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Lob;

public interface ContactViewSmall {
    // @Value("#{target.id}")
    Long getId();
    @Schema(description = "companyname lastname firstname")
    String getName();
    @Schema(description = "The thumbnail of the contact", type = "string", format = "binary")
    @Lob
    byte[] getThumbNail();
    @Schema(description = "The content type like image/jpeg or image/png of the thumbnail or photo")
    String getPhotoContentType();
    @Schema(description = "Aggregated email adresses")
    String getAddressesAggregate();
    @Schema(description = "Aggregated phone numbers")
    String getPhonesAggregate();
    @Schema(description = "Aggregated email addresses")
    String getEmailsAggregate();
}

