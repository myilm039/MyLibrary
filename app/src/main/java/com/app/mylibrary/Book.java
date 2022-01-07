package com.app.mylibrary;

public class Book {

    private String title;
    private String subtitle;
    private String authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private int pageCount;
    private String category;
    private int id;
    private boolean isInWL =false;
    private boolean isInCollection =false;



    public int getID() {
        return id;
    }

    private String thumbnail;
    private String previewLink;
    private String buyLink;
    private String viewability;

    private String comment;
    private float rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getAuthors(){return authors;}

    public boolean getWLStatus(){return isInWL;}

    public boolean getCollectionStatus(){return isInCollection;}

    public void setWLStatus(boolean status){

        isInWL = status;
    }

    public void setCollectionStatus(boolean status){

        isInCollection = status;

    }



    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getViewability() {
        return viewability;
    }


    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {

        if(description.isEmpty()){

            return "No description available.";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }


    public String getBuyLink() {
        return buyLink;
    }

    public String getCategory() {
        return category;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book(int id,String title, String subtitle, String authors, String publisher,
                String publishedDate, String description, int pageCount, String thumbnail,
                String previewLink, String buyLink, String viewability,
                String comment, float rating, String category) {
        this.id=id;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.thumbnail = thumbnail;
        this.previewLink = previewLink;
        this.buyLink = buyLink;
        this.viewability=viewability;
        this.comment=comment;
        this.rating = rating;
        this.category = category;

    }

}
