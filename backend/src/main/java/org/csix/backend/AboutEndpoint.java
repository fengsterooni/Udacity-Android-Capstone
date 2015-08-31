package org.csix.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;

import static org.csix.backend.OfyService.ofy;

// http://rominirani.com/2014/08/26/gradle-tutorial-part-9-cloud-endpoints-persistence-android-studio/

@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.csix.org",
                ownerName = "backend.csix.org",
                packagePath = ""
        )
)

public class AboutEndpoint {
    public AboutEndpoint() {
    }

    @ApiMethod(name = "listAbout")
    public ArrayList<About> listAbout() {

        Query<About> query = ofy().load().type(About.class);

        ArrayList<About> records = new ArrayList<About>();

        for (About e : query) {
            records.add(e);
        }
        return records;
    }

    /**
     * This inserts a new <code>About</code> object.
     *
     * @param About The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertAbout")
    public About insertAbout(About About) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present
        if (About.getId() != null) {
            if (findRecord(About.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(About).now();
        return About;
    }

    /**
     * This updates an existing <code>About</code> object.
     *
     * @param About The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateAbout")
    public About updateAbout(About About) throws NotFoundException {
        if (findRecord(About.getId()) == null) {
            throw new NotFoundException("About Record does not exist");
        }
        ofy().save().entity(About).now();
        return About;
    }

    /**
     * This deletes an existing <code>About</code> object.
     *
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeAbout")
    public void removeAbout(@Named("id") Long id) throws NotFoundException {
        About record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("About Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    /**
     * This deletes all existing <code>About</code> object.
     */
    @ApiMethod(name = "removeAllAbouts")
    public void removeAllAbouts() throws NotFoundException {
        Query<About> abouts = ofy().load().type(About.class);
        for (About about : abouts)
            ofy().delete().entity(about).now();
    }

    //Private method to retrieve a <code>About</code> record
    private About findRecord(Long id) {
        return ofy().load().type(About.class).id(id).now();
        //or return ofy().load().type(About.class).filter("id",id).first.now();
    }

}
