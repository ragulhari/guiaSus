package com.ragulhari.guiaSus.listObjects;

@SuppressWarnings("unused")
public class OpinionItem {
    public int rating;
    public String opinionText;

    public OpinionItem(String pCnesId, String pUserEmail, int pRating, String pOpinionText) {
        this.rating = pRating;
        String userEmail = pUserEmail;
        String cnesId = pCnesId;
        this.opinionText = pOpinionText;
    }
}
