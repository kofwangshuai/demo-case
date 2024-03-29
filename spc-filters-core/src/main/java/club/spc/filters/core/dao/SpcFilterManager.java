package club.spc.filters.core.dao;//package com.clubfactory.center.demo1.core.spc.dao;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @ClassName:SpcFilterManager
// * @Description: Spring boot 规约大于配置
// * @Author kof.wang
// * @Date : 2019-10-25 03:01
// * @Version 1.0.0
// **/
//@Deprecated
//public class SpcFilterManager {
//
//    private static final Logger LOG = LoggerFactory.getLogger(SpcFilterManager.class);
//
//    String[] aDirectories;
//    int pollingIntervalSeconds;
//    Thread poller;
//    boolean bRunning = true;
//
//    static FilenameFilter FILENAME_FILTER;
//
//    static SpcFilterManager INSTANCE;
//
//    private SpcFilterManager() {
//    }
//
//    public static void setFilenameFilter(FilenameFilter filter) {
//        FILENAME_FILTER = filter;
//    }
//
//    /**
//     * Initialized the GroovyFileManager.
//     *
//     * @param pollingIntervalSeconds the polling interval in Seconds
//     * @param directories            Any number of paths to directories to be polled may be specified
//     * @throws IOException
//     * @throws IllegalAccessException
//     * @throws InstantiationException
//     */
//    public static void init(int pollingIntervalSeconds, String... directories) throws Exception, IllegalAccessException, InstantiationException {
//        if (INSTANCE == null) INSTANCE = new SpcFilterManager();
//
//        INSTANCE.aDirectories = directories;
//        INSTANCE.pollingIntervalSeconds = pollingIntervalSeconds;
//        INSTANCE.manageFiles();
//        INSTANCE.startPoller();
//
//    }
//
//    public static SpcFilterManager getInstance() {
//        return INSTANCE;
//    }
//
//    /**
//     * Shuts down the poller
//     */
//    public static void shutdown() {
//        INSTANCE.stopPoller();
//    }
//
//
//    void stopPoller() {
//        bRunning = false;
//    }
//
//    void startPoller() {
//        poller = new Thread("GroovyFilterFileManagerPoller") {
//            public void run() {
//                while (bRunning) {
//                    try {
//                        sleep(pollingIntervalSeconds * 1000);
//                        manageFiles();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        poller.setDaemon(true);
//        poller.start();
//    }
//
//    /**
//     * Returns the directory File for a path. A Runtime Exception is thrown if the directory is in valid
//     *
//     * @param sPath
//     * @return a File representing the directory path
//     */
//    public File getDirectory(String sPath) {
//        File  directory = new File(sPath);
//        if (!directory.isDirectory()) {
//            URL resource = SpcFilterManager.class.getClassLoader().getResource(sPath);
//            try {
//                directory = new File(resource.toURI());
//            } catch (Exception e) {
//                LOG.error("Error accessing directory in classloader. path=" + sPath, e);
//            }
//            if (!directory.isDirectory()) {
//                throw new RuntimeException(directory.getAbsolutePath() + " is not a valid directory");
//            }
//        }
//        return directory;
//    }
//
//    /**
//     * Returns a List<File> of all Files from all polled directories
//     *
//     * @return
//     */
//    List<File> getFiles() {
//        List<File> list = new ArrayList<File>();
//        for (String sDirectory : aDirectories) {
//            if (sDirectory != null) {
//                File directory = getDirectory(sDirectory);
//                File[] aFiles = directory.listFiles(FILENAME_FILTER);
//                if (aFiles != null) {
//                    list.addAll(Arrays.asList(aFiles));
//                }
//            }
//        }
//        return list;
//    }
//
//    /**
//     * puts files into the SpcFilterLoader. The SpcFilterLoader will only addd new or changed filters
//     *
//     * @param aFiles a List<File>
//     * @throws IOException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     */
//    void processGroovyFiles(List<File> aFiles) throws Exception, InstantiationException, IllegalAccessException {
//
//        for (File file : aFiles) {
//            SpcFilterLoader.getInstance().putFilter(file);
//        }
//    }
//
//    void manageFiles() throws Exception, IllegalAccessException, InstantiationException {
//        List<File> aFiles = getFiles();
//        processGroovyFiles(aFiles);
//    }
//
//
//}
