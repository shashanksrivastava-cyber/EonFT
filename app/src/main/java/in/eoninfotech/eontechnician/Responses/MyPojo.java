package in.eoninfotech.eontechnician.Responses;

import java.util.ArrayList;

/**
 * Created by root on 26/11/18.
 */

public class MyPojo  {

        private ArrayList list;

        private String type;

        public ArrayList<MyPojo> getList ()
        {
            return list;
        }

    public void setList (ArrayList list)
    {
        this.list = list;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [list = "+list+", type = "+type+"]";
    }
}
