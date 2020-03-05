package net.whg.nghaste.codeflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a utility class which allows new data types to be created
 * dynamically by a name without worrying about creating a new class for each.
 * Data types created through this class have support for heirarchies, allowing
 * them to automatically be casted up.
 * <p>
 * This object heirarchy comes included with a set of primative object types.
 * 
 * <pre>
 * -Object
 * |-Number
 * | |-Integer
 * |-Boolean
 * |-String
 * </pre>
 */
public class ObjectHeirarchy implements Iterable<DataObj>
{
    private final List<DataObj> objects = new ArrayList<>();

    /**
     * Creates a new object heirarchy and initializes it with primative data types.
     */
    public ObjectHeirarchy()
    {
        addDataType("Object");
        addDataType("Number");
        addDataType("Boolean");
        addDataType("String");
        addDataType("Integer", getObject("Number"));
    }

    /**
     * Creates a new data type and adds it to this object heirarchy.
     * 
     * @param name
     *     - The name of the data object to create.
     * @param parents
     *     - The parents this new data type should extend from. Data types are
     *     always allowed to cast up to parent objects, recursively. This list
     *     should not contain the root object, as that is added automatically.
     * @return The newly created data type.
     * @throws IllegalArgumentException
     *     If the root object is in the parents list, or if another data object with
     *     the same name exists.
     */
    public DataObj addDataType(String name, DataObj... parents)
    {
        DataObj root = getRootObject();
        for (DataObj d : parents)
            if (d == root)
                throw new IllegalArgumentException("Parent list cannot contain the root object!");

        if (getObject(name) != null)
            throw new IllegalArgumentException("An object with the name ''" + name + "'' already exists!");

        DataObj[] p = new DataObj[parents.length + 1];
        p[0] = getRootObject();

        for (int i = 0; i < parents.length; i++)
            p[i + 1] = parents[i];

        DataObj dataObj = new DataObj(name, p);
        objects.add(dataObj);

        return dataObj;
    }

    /**
     * Gets the root data type. All other data types extend from this data type.
     * 
     * @return The root data type.
     */
    public DataObj getRootObject()
    {
        return objects.get(0);
    }

    /**
     * Finds and returns a data object by its name.
     * 
     * @param name
     *     - The name of the data object.
     * @return The data object with the given name, or null if the object could not
     *     be found.
     */
    public DataObj getObject(String name)
    {
        for (DataObj obj : objects)
            if (obj.getName()
                   .equals(name))
                return obj;

        return null;
    }

    @Override
    public Iterator<DataObj> iterator()
    {
        return objects.iterator();
    }
}
