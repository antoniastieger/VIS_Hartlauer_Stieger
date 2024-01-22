package at.fhooe.sail.vis.general;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines methods for requesting environmental data from a service.
 * It extends the Remote interface to support remote invocation.
 */
public interface IEnvService extends Remote {

    /**
     * Requests the available types of environmental data.
     *
     * @return An array of strings representing the available environment data types.
     * @throws RemoteException If a remote communication error occurs.
     */
    String[] requestEnvironmentDataTypes() throws RemoteException;

    /**
     * Requests environmental data for a specific type.
     *
     * @param _type The type of environmental data to request.
     * @return The environmental data corresponding to the specified type.
     * @throws RemoteException If a remote communication error occurs.
     */
    EnvData requestEnvironmentData(String _type) throws RemoteException;

    /**
     * Requests all available environmental data.
     *
     * @return An array of EnvData objects representing all available environmental data.
     * @throws RemoteException If a remote communication error occurs.
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