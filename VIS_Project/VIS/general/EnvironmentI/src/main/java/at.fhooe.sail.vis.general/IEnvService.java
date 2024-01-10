package at.fhooe.sail.vis.general;

import java.rmi.RemoteException;

/**
 * Interface for requesting environmental data from a service.
 */
public interface IEnvService {

    /**
     * Requests available environment data types.
     *
     * @return An array of environment data types.
     */
    String[] requestEnvironmentDataTypes() throws RemoteException;

    /**
     * Requests environmental data for a specific type.
     *
     * @param _type The type of environmental data to request.
     * @return The requested environmental data.
     */
    EnvData requestEnvironmentData(String _type) throws RemoteException;

    /**
     * Requests all available environmental data.
     *
     * @return An array of all available environmental data.
     */
    EnvData[] requestAll()throws RemoteException;
}

/*
package at.fhooe.sail.vis.general;

public interface IEnvService {
    String[] requestEnvironmentDataTypes();
    EnvData requestEnvironmentData(String _type);
    EnvData[] requestAll();
}
*/