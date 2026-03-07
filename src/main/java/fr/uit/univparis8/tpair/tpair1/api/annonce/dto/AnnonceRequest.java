package fr.uit.univparis8.tpair.tpair1.api.annonce.dto;

import fr.uit.univparis8.tpair.tpair1.enums.AnnonceStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnnonceRequest {
    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 256)
    private String description;

    @NotBlank
    @Size(max = 64)
    private String adress;

    @NotBlank
    @Email
    @Size(max = 64)
    private String mail;

    private AnnonceStatus status;
    private Long categoryId;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAdress() { return adress; }
    public void setAdress(String adress) { this.adress = adress; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public AnnonceStatus getStatus() { return status; }
    public void setStatus(AnnonceStatus status) { this.status = status; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
