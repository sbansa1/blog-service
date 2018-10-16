package blog.service;

import blog.service.blog.service.dao.PostDao;
import blog.service.blog.service.model.Post;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("comment")
public class PostService {

     private PostDao ds;


    @GET
    @Path("/{User_ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getPost(@PathParam("User_ID") int User_ID){

         Post p = null;

        try{
            ds = new PostDao();
        }catch (Exception e){
            e.printStackTrace();
        }

        p = ds.selectPost(User_ID);
        //System.out.println(p);
        return p;



    }


    @POST
    @Path("secured/insertcomment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response insertPost(Post entity) {
        try {
            ds = new PostDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String newID = String.valueOf(p.getComment());

        ds.createPost(entity.getComment());

//        URI uri = uriInfo.getAbsolutePathBuilder().path(" ").build();
        return Response.status(200).build();

    }


    @PUT
    @Path("secured/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response updatePost(Post entity){
        //User u = new User();

        try {
            ds = new PostDao();

        } catch (Exception e) {
            e.printStackTrace();
        }

            try {
                ds.updatePost(entity.getComment(),entity.getPostId());
                return Response.status(Response.Status.CREATED).entity(entity).build();
            }catch (Exception e){
            return Response.serverError().build();
            }

            //return Response.status(200).build();



    }

    @DELETE
    @Path("secured/{postID}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("postID") int postID, @PathParam("userId") int userId){

        try {
            ds = new PostDao();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ds.deletePost(postID,userId);
            return  Response.status(Response.Status.MOVED_PERMANENTLY).build();
        }catch (Exception e){
            return  Response.status(500,e.getMessage()).build();
        }



    }






}
