package com.jettra.plugin.example.library.model;

import com.jettra.plugin.example.library.entity.Book;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.ViewSelectOne;

@JettraViewModel
@ModelToRecordConversor(goal = Book.class)
public class BookModel {

    @PropertiesInRecord
    private String id;

    @PropertiesInRecord
    private String title;

    @PropertiesInRecord(field = "authorId")
    //@SelectOne(model = AuthorModel.class, repository = com.jettra.example.repository.library.AuthorRepository.class, itemValue = "id", itemLabel = "name")
    @ViewSelectOne(label = "nombre", source = "ArticuloRepository", method = "findAll")
    private String authorId;

    @PropertiesInRecord(field = "publisherId")
    //@SelectOne(model = PublisherModel.class, repository = com.jettra.example.repository.library.PublisherRepository.class, itemValue = "id", itemLabel = "name")
    @ViewSelectOne(label = "name", source = "PublisherRepository", method = "findAll")
    private String publisherId;

    @PropertiesInRecord
    private Integer year;

    public BookModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
