package com.ikonholdings.ikoniconnects_subscriber;

public class Comments_Model {
    private int Userimage;
    private String UserName, CommentDate, Comment;

    public Comments_Model(int userimage, String userName, String commentDate, String comment) {
        Userimage = userimage;
        UserName = userName;
        CommentDate = commentDate;
        Comment = comment;
    }

    public int getUserimage() {
        return Userimage;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCommentDate() {
        return CommentDate;
    }

    public String getComment() {
        return Comment;
    }
}
