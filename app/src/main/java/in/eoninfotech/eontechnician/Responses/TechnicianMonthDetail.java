package in.eoninfotech.eontechnician.Responses;

import java.io.Serializable;

/**
 * Created by root on 18/10/18.
 */

public class TechnicianMonthDetail implements Serializable {

    private String id;

    private String location;

    private String name;

    private String month;

    private String image;

    private String year;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getMonth ()
    {
        return month;
    }

    public void setMonth (String month)
    {
        this.month = month;
    }

    public String getImage ()
    {
        return image;
    }

    public void setImage (String image)
    {
        this.image = image;
    }

    public String getYear ()
    {
        return year;
    }

    public void setYear (String year)
    {
        this.year = year;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", location = "+location+", name = "+name+", month = "+month+", image = "+image+", year = "+year+"]";
    }
}
