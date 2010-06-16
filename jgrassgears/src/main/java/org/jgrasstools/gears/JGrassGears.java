/*
 * JGrass - Free Open Source Java GIS http://www.jgrass.org 
 * (C) HydroloGIS - www.hydrologis.com 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Foundation, Inc., 59
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jgrasstools.gears;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import oms3.Access;
import oms3.ComponentAccess;
import oms3.annotations.Description;
import oms3.annotations.Execute;

import org.jgrasstools.gears.libs.modules.ClassField;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;

/**
 * Class presenting modules names and classes.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("nls")
public class JGrassGears {

    /**
     * A {@link LinkedHashMap map} of all the class names and the class itself.
     */
    public static final LinkedHashMap<String, Class< ? >> moduleName2Class = new LinkedHashMap<String, Class< ? >>();

    /**
     * A {@link LinkedHashMap map} of all the class names and their fields.
     */
    public static final LinkedHashMap<String, List<ClassField>> moduleName2Fields = new LinkedHashMap<String, List<ClassField>>();

    /**
     * An array of all the fields used in the modules.
     */
    public static String[] allFields = null;

    /**
     * An array of all the class names of the modules.
     */
    public static String[] allClasses = null;

    static {

        try {
            URL url = ClasspathUrlFinder.findClassBase(JGrassGears.class);
            AnnotationDB db = new AnnotationDB();
            db.scanArchives(url);

            Map<String, Set<String>> annotationIndex = db.getAnnotationIndex();
            Set<String> simpleClasses = annotationIndex.get(Execute.class.getName());
            for( String className : simpleClasses ) {
                int lastDot = className.lastIndexOf('.');
                String name = className.substring(lastDot + 1);
                Class< ? > clazz = Class.forName(className);
                moduleName2Class.put(name, clazz);
            }

            /*
             * extract all classes and fields
             */
            List<String> classNames = new ArrayList<String>();
            List<String> fieldNamesList = new ArrayList<String>();

            Set<Entry<String, Class< ? >>> moduleName2ClassEntries = moduleName2Class.entrySet();
            for( Entry<String, Class< ? >> moduleName2ClassEntry : moduleName2ClassEntries ) {
                String moduleName = moduleName2ClassEntry.getKey();
                Class< ? > moduleClass = moduleName2ClassEntry.getValue();

                classNames.add(moduleName);

                List<ClassField> tmpfields = new ArrayList<ClassField>();
                Object annotatedObject = moduleClass.newInstance();
                ComponentAccess cA = new ComponentAccess(annotatedObject);
                Collection<Access> inputs = cA.inputs();
                for( Access access : inputs ) {
                    Field field = access.getField();
                    String name = field.getName();
                    Description descriptionAnnot = field.getAnnotation(Description.class);
                    String description = name;
                    if (descriptionAnnot != null) {
                        description = descriptionAnnot.value();
                        if (description == null) {
                            description = name;
                        }
                    }
                    Class< ? > fieldClass = field.getType();
                    ClassField cf = new ClassField();
                    cf.isIn = true;
                    cf.fieldName = name;
                    cf.fieldDescription = description;
                    cf.fieldClass = fieldClass;
                    cf.parentClass = moduleClass;
                    if (!fieldNamesList.contains(name)) {
                        fieldNamesList.add(name);
                    }
                    tmpfields.add(cf);

                }
                Collection<Access> outputs = cA.outputs();
                for( Access access : outputs ) {
                    Field field = access.getField();
                    String name = field.getName();
                    Description descriptionAnnot = field.getAnnotation(Description.class);
                    String description = name;
                    if (descriptionAnnot != null) {
                        description = descriptionAnnot.value();
                        if (description == null) {
                            description = name;
                        }
                    }
                    Class< ? > fieldClass = field.getType();
                    ClassField cf = new ClassField();
                    cf.isOut = true;
                    cf.fieldName = name;
                    cf.fieldDescription = description;
                    cf.fieldClass = fieldClass;
                    cf.parentClass = moduleClass;
                    if (!fieldNamesList.contains(name)) {
                        fieldNamesList.add(name);
                    }
                    tmpfields.add(cf);
                }
                moduleName2Fields.put(moduleName, tmpfields);
            }
            Collections.sort(fieldNamesList);
            allFields = (String[]) fieldNamesList.toArray(new String[fieldNamesList.size()]);
            Collections.sort(classNames);
            allClasses = (String[]) classNames.toArray(new String[classNames.size()]);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void main( String[] args ) throws IOException {
        Set<Entry<String, Class< ? >>> entrySet = moduleName2Class.entrySet();
        for( Entry<String, Class< ? >> entry : entrySet ) {
            System.out.println(entry.getKey() + " - " + entry.getValue().getCanonicalName());
        }

        List<ClassField> list = moduleName2Fields.get("LineSmoother");
        for( ClassField classField : list ) {
            System.out.println(classField);
        }
    }

}
