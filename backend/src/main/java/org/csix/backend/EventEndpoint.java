// http://rominirani.com/2014/08/26/gradle-tutorial-part-9-cloud-endpoints-persistence-android-studio/

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

public class EventEndpoint {
    public EventEndpoint() {
    }

    @ApiMethod(name = "listEvent")
    public ArrayList<Event> listEvent() {

        Query<Event> query = ofy().load().type(Event.class);

        ArrayList<Event> records = new ArrayList<>();

        for (Event e : query) {
            records.add(e);
        }
        return records;
    }

    /**
     * This inserts a new <code>Event</code> object.
     *
     * @param Event The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertEvent")
    public Event insertEvent(Event Event) throws ConflictException {
        //If if is not null, then check if it exists. If yes, throw an Exception
        //that it is already present
        if (Event.getId() != null) {
            if (findRecord(Event.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put
        ofy().save().entity(Event).now();
        return Event;
    }

    /**
     * This updates an existing <code>Event</code> object.
     *
     * @param Event The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateEvent")
    public Event updateEvent(Event Event) throws NotFoundException {
        if (findRecord(Event.getId()) == null) {
            throw new NotFoundException("Event Record does not exist");
        }
        ofy().save().entity(Event).now();
        return Event;
    }

    /**
     * This deletes an existing <code>Event</code> object.
     *
     * @param id The id of the object to be deleted.
     */
    @ApiMethod(name = "removeEvent")
    public void removeEvent(@Named("id") Long id) throws NotFoundException {
        Event record = findRecord(id);
        if (record == null) {
            throw new NotFoundException("Event Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    /**
     * This deletes all existing <code>Event</code> object.
     */
    @ApiMethod(name = "removeAllEvents")
    public void removeAllEvents() throws NotFoundException {
        Query<Event> events = ofy().load().type(Event.class);
        for (Event event : events)
            ofy().delete().entity(event).now();
    }

    //Private method to retrieve a <code>Event</code> record
    private Event findRecord(Long id) {
        return ofy().load().type(Event.class).id(id).now();
        //or return ofy().load().type(Event.class).filter("id",id).first.now();
    }

}
