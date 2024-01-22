package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class Sensors {
    private String[] sensorTypes;
    private List<EnvData> envDataList;

    public String[] getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(String[] sensorTypes) {
        this.sensorTypes = sensorTypes;
    }

    public void setEnvDataList(EnvData[] envData) {
        this.envDataList = envDataList;
    }
}
