package blog.service.blog.service.model;

        import java.util.ArrayList;
        import java.util.Collection;
        import javax.persistence.*;
        import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@Entity
@org.hibernate.annotations.Entity(selectBeforeUpdate = true )
public class User {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;
    //@Column(unique = true)
    private String email;
    private String passWord;



   // @Embedded
   // @ElementCollection
    //@JoinTable(name ="User_Post", joinColumns = @JoinColumn(name = "userId"))
   // @GenericGenerator(name = "sequence-gen",strategy = "sequence")
 //   @CollectionId(columns = {@Column(name = "PostID")}, type = @Type(type ="long"), generator = "sequence-gen" )

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Post> post = new ArrayList <Post>();



    public Collection <Post> getPost() {
        return post;
    }

    public void setPost(Collection <Post> post) {
        this.post = post;
    }


    public User(){

    }

    public User(String userName, String email, String passWord) {
        this.userName = userName;
        this.email = email;
        this.passWord = passWord;

    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }





}
