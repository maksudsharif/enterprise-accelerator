package com.maksudsharif.repository.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Region("Records")
@Table(name = "records")
public class Record implements InstanceIdAware, Serializable
{
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    Record(){}

    public Record(String message)
    {
        this.message = message;
    }

    @PersistenceConstructor
    public Record(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        return new EqualsBuilder()
                .append(id, record.id)
                .append(message, record.message)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(message)
                .toHashCode();
    }

    @Override
    public String toString()
    {
        return "Record{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
