package blog.service.blog.service.authentication;

import blog.service.blog.service.utility.HibernateUtil;
import blog.service.blog.service.model.User;
import org.glassfish.jersey.internal.util.Base64;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Method;


@Provider
public class AuthenticationFIlter implements ContainerRequestFilter {


    private static final String AURHORIZATION_HEADER_KEY = "Authorization";
    private static final String AURHORIZATION_PREFIX = "Basic ";
    private static final String SECURED_PATH_PREFIX = "secured";
    @Context
    private ResourceInfo resourceInfo;
    private User user = new User();
    private String userN;
    private String pass;
    private String rolesSet;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {


        if (containerRequestContext.getUriInfo().getPath().contains(SECURED_PATH_PREFIX)) {

            Method method = resourceInfo.getResourceMethod();
            if (!method.isAnnotationPresent(PermitAll.class)) {
                if (method.isAnnotationPresent(DenyAll.class)) {
                    Response.status(Response.Status.UNAUTHORIZED).entity("Not allowed to access the Resource");
                    return;
                }

                // String authHeader1 = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

                List <String> authHeader = containerRequestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);

                  if(authHeader==null && authHeader.isEmpty()){
                   containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                  }

                if (authHeader.size() > 0 && authHeader != null) {

                    String authToken = authHeader.get(0);
                    authToken = authToken.replaceFirst(AURHORIZATION_PREFIX, "");
                    String base64DecodedString = Base64.decodeAsString(authToken);
                    StringTokenizer tokenizer = new StringTokenizer(base64DecodedString, ":");
                    String userName = tokenizer.nextToken();
                    String passWord = tokenizer.nextToken();


                    if (method.isAnnotationPresent(RolesAllowed.class)) {
                        RolesAllowed rolesAnnotatin = method.getAnnotation(RolesAllowed.class);

                        Set <String> roleSet = new HashSet <String>(Arrays.asList(rolesAnnotatin.value()));

                        checkUser(userName, passWord, roleSet);

                        if (userName.equals(userN) && passWord.equals(pass)) {

                            return;
                        } else {
                            Response.status(Response.Status.UNAUTHORIZED).entity("Invalid User").build();
                        }


                    }

                }


            } else {
                Response unauthorized = Response.status(Response.Status.FORBIDDEN).entity(Response.serverError()).build();

                containerRequestContext.abortWith(unauthorized);
            }

        }

    }

    public void checkUser(String userName, String passWord, Set <String> roleSet) {

        Session session = null;
        String roles = null;

        String hql = "from User where userName = :userName AND passWord =:passWord";
        List <User> row = new ArrayList <>();
        try {

            session = HibernateUtil.getInstance().getSession();

            session.beginTransaction();
            Query query = session.createQuery(hql);

            query.setParameter("userName", userName);
            query.setParameter("passWord", passWord);

            row = query.list();

            for (User u : row) {
                if (userName.equals(u.getUserName()) && passWord.equals(u.getPassWord())) {
                    userN = u.getUserName();
                    pass = u.getPassWord();

                    roles = "ADMIN";


                }
            }

            if (roleSet.contains(roles)) {
                return;
            }


            session.getTransaction().commit();


        } catch (Exception e) {
            e.printStackTrace();
        }
           finally
        {
            session.close();
        }


    }
}
