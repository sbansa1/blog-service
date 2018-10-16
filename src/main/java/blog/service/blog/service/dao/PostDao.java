package blog.service.blog.service.dao;

import blog.service.blog.service.model.Post;
import blog.service.blog.service.model.User;
import blog.service.blog.service.utility.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostDao {



    private Session session;
    public PostDao() {


    }





    public Post createPost(String comment) {




        Date date = new Date();
        User user = new User();
        Post post = new Post();



        try {
            session = HibernateUtil.getInstance().getSession();
            session.beginTransaction();
            user.setUserName("Saurabh");
            user.setEmail("SaurabhBansal@gmail.com");
            user.setPassWord("12345");
            post.setComment(comment);
            post.setPostDate(date);

            user.getPost().add(post);



            session.persist(user);
           // session.save(post);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.fillInStackTrace();
        } finally {

            session.close();
        }

        return post;


    }


    public Post selectPost(int userId) {


        session  = null;
        //String hql = "select e from User e inner join e.post where e.userId = :userId" ;
        String hql1 = "from Post p where p.user.id = :userId";

        List <Post> row = new ArrayList <>();
        Post post = new Post();

        try {
            session = HibernateUtil.getInstance().getSession();
            session.beginTransaction();
            Query query = session.createQuery(hql1);

            query.setParameter("userId", userId);
            row = query.list();


            for (Post p : row) {
                post = new Post(p.getPostDate(), p.getComment());
            }


            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
session.close();


        }
        return post;
    }

    public Post updatePost(String comment, int id) {

        session = null;
       Post p = new Post();


        try {
            session = HibernateUtil.getInstance().getSession();
            session.beginTransaction();

            session.get(Post.class,id);
            p.setComment(comment);
            session.update(p);

            session.getTransaction().commit();

        } catch (Exception e) {
            e.fillInStackTrace();
        } finally {


            session.close();
        }

        return p;
    }


    public Post deletePost(int postid, int userId) {

        Post p = new Post();
        User user = new User();
        session = null;


        try {
            session = HibernateUtil.getInstance().getSession();
            session.beginTransaction();
            p.setPostId(postid);
            user.setUserId(userId);
            user.getPost().add(p);

          Object persistentInstance =  session.load(User.class,userId);

          if(persistentInstance!=null) {
              session.delete(persistentInstance);
          }
            session.getTransaction().commit();
            Response.status(Response.Status.ACCEPTED).entity(Response.ok()).build();
        }catch (Exception e){
            e.printStackTrace();
           Response.status(500,e.getMessage()).build();

        }finally {
            session.close();
        }
        return p;





    }
}
