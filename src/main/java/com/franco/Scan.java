package com.franco;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描
 *
 * @author franco
 */
public class Scan {

    // TODO 异常处理
    public static Set<Class<?>> getClassed(String pack) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = pack.replace(".", "/");
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if("file".equalsIgnoreCase(protocol)) {
                    String fileName = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    packageName = pack;
                    findAndAddClassesInPackageByFile(packageName, fileName, recursive, classes);
                }
                else if("jar".equalsIgnoreCase(protocol)) {
                    JarFile jar;
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while(entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String name = jarEntry.getName();
                        if(name.charAt(0) == '/') {
                            name = name.substring(1);
                        }
                        if(name.startsWith(packageDirName)) {
                            int idx = name.lastIndexOf("/");
                            if(idx != -1) {
                                packageName = name.substring(0, idx).replace("/", ".");
                                if(name.endsWith(".class") && !jarEntry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + name));
                                    } catch (ClassNotFoundException e) {

                                    }
                                }
                            }
                        }
                    }
                }
                else if("class".equalsIgnoreCase(protocol)) {
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(url.getFile()));
                    } catch (ClassNotFoundException e) {

                    }
                }
            }
        } catch (IOException e) {

        }
        return classes;
    }

    // TODO 异常处理
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                  String fileName,
                                                  final boolean recursive,
                                                  Set<Class<?>> classes) {
        File dir = new File(fileName);
        if(!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] dirFiles = dir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || file.getName().endsWith(".class");
            }
        });

        for(File file : dirFiles) {
            if(file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6); // - ".class"
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
                } catch (ClassNotFoundException e) {

                }
            }
        }
    }
}
