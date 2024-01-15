package ua.hudyma.db;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** Pool singleton
 * @author IC
 * inline comments by Yurko Hudyma
 */

/** This class creates a pool of connections via HashMap,
 * not to be re-opened and closed every time, which takes time and resources,
 * but to organise DB-connectivity and improve scalability.
 * Connection is implemented via the only ONE instance of the Pool class
 * by means of private constructor (thus the Singleton Pattern is provided)
 * @author Losth */

public class Pool {
	/** @param stores service information upon logging key events */
    private static final Logger LOG = Logger.getLogger(Pool.class);
    /** @param timeout for reconnect */
    private static final int GET_CONNECTION_MILLIS = 1000;
    /** @param local storage path for DB-credentials */
    private static final String PROPERTIES_PATH = "/pool-conf.properties";
    /** @param Single instance of Pool class that provides Singleton pattern */
    private static Pool INSTANCE;


    /** @category This method creates INSTANCE when null and provides synchronisation for it */
    public static Pool getInstance() {
        if (INSTANCE == null)
            synchronized (Pool.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Pool();
                }
            }
        return INSTANCE;
    }
    /** @param This map instance declares a collection of connections ("connection = true" INSTEAD of "null = false" pair) */
    private Map<Connection, Boolean> connections;
    /** @param URL of the DB */
    private final String URL;
    /** @param USER for DB access */
    private final String USER;
    /** @param PASSWORD for DB access */
    private final String PASSWORD;

    /**
     * Creates pool: HashMap with connections as keys.
     * A private construct for a single access (Singleton)
     */
    private Pool() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream(PROPERTIES_PATH));
            Class.forName(properties.getProperty("db.driver"));
            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.password");
            int capacity = Integer.parseInt(properties.getProperty("db.poolsize"));
            connections = new HashMap<>(capacity);
            for (int i = 0; i < capacity; i++) {
                connections.put(createConnection(), true);
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /** this method of PoolConnection class creates a sole connection with retrieved credentials, 
     * invoking a construct from this construct */
    private PoolConnection createConnection() throws SQLException {
        return new PoolConnection(DriverManager.getConnection(URL, USER, PASSWORD), this);
    }

    /** retrieves a previously stored connection
     * from a pool */

    @SuppressWarnings("finally")
    public Connection getConnection() {
        Connection result = null;
        for (Map.Entry<Connection, Boolean> entry : connections.entrySet()) {
        	if (entry.getValue()) {
                synchronized (this) {
                    if (entry.getValue()) {
                        Connection key = entry.getKey();
                        connections.put(key, false);
                        result = key;
                    }
                }
            }
        }
        if (result == null) {
            try {
                Thread.sleep(GET_CONNECTION_MILLIS);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage());
                LOG.info("Retrying to get connection");
            } finally {
                return getConnection();
            }
        } else {
            return result;
        }
    }
    /** This method intercepts a <b>close</b> method from ConnectionPool, 
     * which is overriden to save connection alive instead of closing. 
     * It is saved in HashMap as available in pool. */
    
    public void free(PoolConnection poolConnection) {    	
        connections.put(poolConnection, true);
    }
}