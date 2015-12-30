package org.csix.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

@Entity
public class Event {
    @Id
    private Long id;
    private Date date;
    private String speaker;
    private String image;
    private String topic;
    private String desc;
    private int type = 0;

    public Event() {}

    public Event(Date date, String speaker, String topic, String desc) {
        setDate(date);
        setSpeaker(speaker);
        setTopic(topic);
        if (desc != null) setDesc(desc);
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    private void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpeaker() {
        return speaker;
    }

    private void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getTopic() {
        return topic;
    }

    private void setTopic(String topic) {
        this.topic = topic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
