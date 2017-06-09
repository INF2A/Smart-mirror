package com.smartmirror.sys;

import com.smartmirror.core.view.AbstractApplication;
import com.smartmirror.sys.view.AbstractSystemApplication;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Erwin on 5/29/2017.
 *
 * Parses every jar in the 'applications' folder.
 * This folder consists of the folders system and user.
 * In these folders are JAR files which this class will load.
 *
 * This class wil then instanti
 *
 */
public class ApplicationParser {

    final File folder = new File("com/smartmirror/sys/applications");
    private Map<String, File> systemApplicationJars = new HashMap<>();
    private Map<String, File> userApplicationJars = new HashMap<>();


    private Map<String, String> applicationClassNames = new HashMap<>();

    private Map<String, AbstractSystemApplication> instantiatedSystemApplications = new HashMap<>();

    public Map<String, AbstractSystemApplication> getSystemApplications() {
        return instantiatedSystemApplications;
    }

    public ApplicationParser() {
        loadApplicationJars(folder);
        instantiateApplications();
    }

    private void instantiateApplications() {
        for(Map.Entry<String, File> entry : systemApplicationJars.entrySet()) {
            try {
                loadApplicationFromClass(entry.getValue(), "system", entry.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(applicationClassNames.keySet());
    }

    private void loadApplicationJars(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if(fileEntry.isDirectory()) {
                loadApplicationJars(fileEntry);
            }
            else {
                String name = fileEntry.getName().split("\\.jar", 2)[0];
                if (fileEntry.getParentFile().getName().equals("system")) {
                    systemApplicationJars.put(name, fileEntry);
                } else {
                    userApplicationJars.put(name, fileEntry);
                }
            }
        }
    }


    private void loadApplicationFromClass(File file, String type, String name) {
        URL downloadURL = null;
        try {
            downloadURL = file.toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL[] downloadURLs = new URL[] { downloadURL };
        URLClassLoader loader = URLClassLoader.newInstance(downloadURLs, getClass().getClassLoader());

        try {
            Class<?> implementingClas;
            if (type.equals("system"))
                implementingClas = findImplementingClasInJarFile(file, AbstractSystemApplication.class, loader);
            else implementingClas = findImplementingClasInJarFile(file, AbstractApplication.class, loader);

            if (implementingClas != null) {
                applicationClassNames.put(implementingClas.getName(), name);
                if (type.equals("system")) {
                    AbstractSystemApplication instance = (AbstractSystemApplication) implementingClas.newInstance();
                    instantiatedSystemApplications.put(name, instance);
                }
//                else {
//                    AbstractApplication instance = (AbstractApplication) implementingClas.newInstance();
//                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Looks inside a jar file and looks for implementing classes of the provided abstract class.
     * </p>
     *
     * @param file
     * The Jar-File containing the classes to scan for implementation of the given abstract class
     * @param aclass
     * The abstract class classes have to implement
     * @param loader
     * The class loader the implementing classes got loaded with
     * @return A {@link List} of implementing classes for the provided abstract class
     * inside jar files of the <em>ClassFinder</em>s class path
     *
     * @throws Exception If during processing of the Jar-file an error occurred
     */
    public Class<?> findImplementingClasInJarFile(File file, Class<?> aclass, ClassLoader loader) throws Exception
    {
        Class<?> implementingClass = null;

        // scan the jar file for all included classes
        for (String classFile : scanJarFileForClasses(file))
        {
            Class<?> clazz;
            try
            {
                // now try to load the class
                if (loader == null)
                    clazz = Class.forName(classFile);
                else
                    clazz = Class.forName(classFile, true, loader);

                // and check if the class implements the provided interface
                if (aclass.isAssignableFrom(clazz) && !clazz.equals(aclass))
                    implementingClass = clazz;
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return implementingClass;
    }

    /**
     * Scans a JAR file for .class-files and returns a {@link List} containing
     * the full name of found classes (in the following form:
     * packageName.className)
     *
     * @param file
     * JAR-file which should be searched for .class-files
     * @return Returns all found class-files with their full-name as a List of
     *         Strings
     * @throws IOException If during processing of the Jar-file an error occurred
     * @throws IllegalArgumentException If either the provided file is null, does
     *                                  not exist or is no Jar file
     */
    public List<String> scanJarFileForClasses(File file) throws IOException, IllegalArgumentException
    {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("Invalid jar-file to scan provided");
        if (file.getName().endsWith(".jar"))
        {
            List<String> foundClasses = new ArrayList<String>();
            try (JarFile jarFile = new JarFile(file))
            {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements())
                {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class"))
                    {
                        String name = entry.getName();
                        name = name.substring(0,name.lastIndexOf(".class"));
                        if (name.indexOf("/")!= -1)
                            name = name.replaceAll("/", ".");
                        if (name.indexOf("\\")!= -1)
                            name = name.replaceAll("\\\\", ".");
                        foundClasses.add(name);
                    }
                }
            }
            return foundClasses;
        }
        throw new IllegalArgumentException("No jar-file provided");
    }
}
