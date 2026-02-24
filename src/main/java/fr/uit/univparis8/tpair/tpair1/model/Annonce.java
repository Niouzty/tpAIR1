package fr.uit.univparis8.tpair.tpair1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "annonce")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 64)
    @Column(length = 64, nullable = false)
    private String title;

    @NotBlank
    @Size(max = 256)
    @Column(length = 256, nullable = false)
    private String description;

    @NotBlank
    @Size(max = 64)
    @Column(length = 64, nullable = false)
    private String adress;

    @NotBlank
    @Email
    @Size(max = 64)
    @Column(length = 64, nullable = false)
    private String mail;

    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnnonceStatus status = AnnonceStatus.DRAFT;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Version
    private Long version;

    public Annonce() {}

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAdress() { return adress; }
    public void setAdress(String adress) { this.adress = adress; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public AnnonceStatus getStatus() { return status; }
    public void setStatus(AnnonceStatus status) { this.status = status; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}