package at.fhooe.sail.vis.general;

public interface IEnvService {
    String[] requestEnvironmentDataTypes();
    EnvData requestEnvironmentData(String _type);
    EnvData[] requestAll();
}