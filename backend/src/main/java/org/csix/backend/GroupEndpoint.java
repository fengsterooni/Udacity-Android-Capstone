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

@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.csix.org",
                ownerName = "backend.csix.org",
                packagePath = ""
        )
)

public class GroupEndpoint {
    public GroupEndpoint() {
    }

    @ApiMethod(name = "listGroup")
    public ArrayList<Group> listGroup() {

        Query<Group> query = ofy().load().type(Group.class);

        ArrayList<Group> records = new ArrayList<Group>();

        for (Group e : query) {
            records.add(e);
        }
        return records;
    }

    /**
     * This inserts a new <code>Group</code> object.
     *
     * @param Group The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertGroup")
    public Group insertGroup(Group Group) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present
        if (Group.getId() != null) {
            if (findRecord(Group.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(Group).now();
        return Group;
    }

    /**
     * This updates an existing <code>Group</code> object.
     *
     * @param Group The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateGroup")
    public Group updateGroup(Group Group) throws NotFoundException {
        if (findRecord(Group.getId()) == null) {
            throw new NotFoundException("Group Record does not exist");
        }
        ofy().save().entity(Group).now();
        return Group;
    }

    /**
     * This deletes an existing <code>Group</code> object.
     *
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeGroup")
    public void removeGroup(@Named("id") Long id) throws NotFoundException {
        Group record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("Group Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    /**
     * This deletes all existing <code>Group</code> object.
     */
    @ApiMethod(name = "removeAllGroups")
    public void removeAllGroups() throws NotFoundException {
        Query<Group> groups = ofy().load().type(Group.class);
        for (Group group : groups)
            ofy().delete().entity(group).now();
    }

    //Private method to retrieve a <code>Group</code> record
    private Group findRecord(Long id) {
        return ofy().load().type(Group.class).id(id).now();
        //or return ofy().load().type(Group.class).filter("id",id).first.now();
    }

}
