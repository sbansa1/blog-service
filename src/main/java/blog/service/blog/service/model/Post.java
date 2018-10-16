package blog.service.blog.service.model;


import blog.service.blog.service.model.User;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@Entity
@XmlRootElement
public class Post {

    @Id @GeneratedValue
    private int postId;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    private Date postDate;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User user;




    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }




    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post(){


    }

    public Post(Date postDate, String comment) {
        this.postDate = postDate;
        this.comment = comment;
    }

    public Post(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
