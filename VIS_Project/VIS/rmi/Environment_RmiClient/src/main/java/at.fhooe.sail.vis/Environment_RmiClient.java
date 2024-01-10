package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.rmi.RemoteException;


public class Environment_RmiClient implements IEnvService {

    @Override
    public String[] requestEnvironmentDataTypes() throws RemoteException {
        return new String[0];
    }

    @Override
    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        return null;
    }

    @Override
    public EnvData[] requestAll() throws RemoteException {
        return new EnvData[0];
    }
}